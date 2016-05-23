package parser;

import combinatorics.SetOps;
import controller.BasicSimulationState;
import controller.SimulationState;
import model.evaluable.*;
import model.societies.BasicPopulation;
import model.societies.Society;
import model.structures.*;
import nu.xom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class reads a PPAL XML input file according to the PPAL Document Type Definition (DTD) and creates the related structures.
 */
public class BasicXMLPPALParser implements PPALParser {

	private Builder parser = new Builder();

	private SimulationState simState = new BasicSimulationState();

	private EvaluationFunction evf;

	public BasicXMLPPALParser(EvaluationFunction evf) {
		this.evf = evf;
	}

	@Override public boolean isDocumentValid(File file) throws IOException {
		try {
			parser.build(file);
			return true;
		}
		catch(ParsingException e) {
			return false;
		}
	}

	@Override public SimulationState parseDocument(File file) throws IllegalArgumentException, IOException {
		Document document;
		try {
			document = parser.build(file);
		} catch (ParsingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		if (document.getDocType().getSystemID() != null) {
			if (!document.getDocType().getSystemID().toLowerCase().contains("ppal.dtd")) {
				throw new IllegalArgumentException("Unexpected DTD.");
			}
		} else {
			throw new IllegalArgumentException("DTD missing.");
		}

		Element root = document.getRootElement();

		//Maps societies' IDs to sizes.
		Map<String, Integer> societiesMap = parseSocieties(root.getFirstChildElement("societies"));
		Set<String> societies = societiesMap.keySet();

		//A <propdef> (i.e. "h0") contains a set of propositions related to a society (i.e. "a.h0").
		Map<String, Set<Proposition>> propsMap;
		try {
			propsMap = parsePropositions(societies, evf, root.getFirstChildElement("propositions"));
		} catch (ParsingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		StateModel stm;
		try {
			stm = parseStates(propsMap, root.getFirstChildElement("states"));
		} catch (ParsingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		State realState;
		try {
			realState = parseRealState(stm, evf, root.getFirstChildElement("realstate"));
		} catch (ParsingException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
		simState.setRealState(realState);

		insertSocieties(societiesMap, propsMap, stm, simState);

		//TODO: Parse manually declared edges as well.

		return simState;
	}

	private Map<String, Integer> parseSocieties(Element rootElement) {
		Map<String, Integer> societies =  new HashMap<String, Integer>();

		Elements elements = rootElement.getChildElements("socdef");
		for(int i=0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String id = element.getAttributeValue("id");
			/*String name = element.getAttributeValue("name");
			if(name == null)
				name = id;*/
			int size = Integer.parseInt(element.getAttributeValue("size"));

			societies.put(id, size);
		}

		return societies;
	}

	private Map<String, Set<Proposition>> parsePropositions(Set<String> societies, EvaluationFunction evf, Element rootElement) throws ParsingException {
		Map<String, Set<Proposition>> propsMap = new HashMap<String, Set<Proposition>>();
		for(String soc : societies) {
			Set<Proposition> propsSet = new HashSet<Proposition>();
			propsMap.put(soc, propsSet);
		}
		Elements elements = rootElement.getChildElements("propdef");
		for(int i=0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String idProp = element.getAttributeValue("id");
			//String name = element.getAttributeValue("name");

			Element elementSoc = element.getFirstChildElement("soc");
			if(elementSoc != null) {
				String idSoc = elementSoc.getAttributeValue("id");

				if (!societies.contains(idSoc)) {
					throw new ParsingException("Society " + idSoc + " undeclared.");
				}

				Set<Proposition> propsSet = propsMap.get(idSoc);
				propsSet.add(new BasicProposition(idProp, evf));
			}
			else {
				if(propsMap.get("nosocietydefined") == null) {
					propsMap.put("nosocietydefined", new HashSet<Proposition>());
				}
				Set<Proposition> nosocietydefined = propsMap.get("nosocietydefined");
				nosocietydefined.add(new BasicProposition(idProp, evf));
			}
		}
		return propsMap;
	}

	private StateModel parseStates(Map<String, Set<Proposition>> propsMap, Element rootElement) throws ParsingException {
		Set<Proposition> allProps = new HashSet<Proposition>();
		for(String key : propsMap.keySet()) {
			Set<Proposition> props = propsMap.get(key);
			allProps.addAll(props);
		}

		Set<State> states = new HashSet<State>();

		Elements elementsComb = rootElement.getChildElements("comb");
		for(int i=0; i < elementsComb.size(); i++) {
			Element elementComb = elementsComb.get(i);

			Element elementRestrictions = elementComb.getFirstChildElement("restrictions");

			/*EXAMPLES OF EXPECTED POWER SETS:
			{ {ah0}, {bh0}, {ch0}, {ah0, bh0}, {ah0, ch0}, {bh0, ch0}, {ah0, bh0, ch0} , ... }*/
			Set<Set<Proposition>> setsProp = SetOps.powerSet(allProps);
			//setsProp.remove(new HashSet<Proposition>()); //Remove empty set.

			for(Set<Proposition> setResult : setsProp) {
				State plausibleState = new BasicState(setResult.toArray(new Proposition[setResult.size()]));
				if(isStateValid(plausibleState, elementRestrictions)) {
					states.add(plausibleState);
				}
			}
		}

		Elements elementsState = rootElement.getChildElements("state");
		for(int i=0; i < elementsState.size(); i++) {
			Element element = elementsState.get(i);
			//TODO: Read manually declared states as well.
		}

		return new BasicStateModel(states.toArray(new State[states.size()]));
	}

	private boolean isStateValid(State state, Element restrictions) {
		if(restrictions == null) {
			return true;
		}
		Set<Proposition> props = state.getPropositions();
		Set<String> propsNames = new HashSet<String>(props.size());
		for(Proposition p : props)
			propsNames.add(p.getName());

		Elements elementsAtLeast = restrictions.getChildElements("atleast");
		for(int i=0; i < elementsAtLeast.size(); i++) {
			Element elementAtLeast = elementsAtLeast.get(i);
			Elements elementsProp = elementAtLeast.getChildElements("prop");
			boolean isOk = false;
			for(int j = 0; j < elementsProp.size(); j++) {
				Element elementProp = elementsProp.get(j);
				for(String propName : propsNames) {
					if(propName.equals(elementProp.getAttributeValue("id"))) {
						isOk = true;
						break;
					}
				}
				if(isOk)
					break;
			}
			if(!isOk)
				return false;
		}

		Elements elementsMutex = restrictions.getChildElements("mutex");
		for(int i=0; i < elementsMutex.size(); i++) {
			Element elementMutex = elementsMutex.get(i);
			Elements elementsProp = elementMutex.getChildElements("prop");
			int count = 0;
			for(int j = 0; j < elementsProp.size(); j++) {
				Element elementProp = elementsProp.get(j);
				for(String propName : propsNames) {
					if(propName.equals(elementProp.getAttributeValue("id"))) {
						count++;
						break;
					}
				}
				if(count > 1) {
					return false;
				}
			}
		}
		return true;
	}

	private State parseRealState(StateModel stm, EvaluationFunction evf, Element rootElement) throws ParsingException {
		Set<Proposition> props = new HashSet<Proposition>();
		Elements elements = rootElement.getChildElements("prop");
		for(int i=0; i < elements.size(); i++) {
			Element element = elements.get(i);
			String id = element.getAttributeValue("id");
			props.add(new BasicProposition(id, evf));
		}
		State realState = new BasicState(props.toArray(new Proposition[props.size()]));

		if(!stm.getStates().contains(realState)) {
			throw new ParsingException("The declared real state is not used anywhere else.");
		}

		return realState;
	}

	private void insertSocieties(Map<String, Integer> socSizes, Map<String, Set<Proposition>> propsMap, StateModel stm, SimulationState simState) {
		Set<State> originalStates = new HashSet<State>(stm.getStates());

		SocietyModel sm = new BasicSocietyModel(stm);
		for(String socName : socSizes.keySet()) {
			Society soc = new BasicPopulation(socName, socName, sm, socSizes.get(socName));
			simState.insertSociety(soc);
			Set<Proposition> socPropsMap = propsMap.get(socName);
			for(State s1 : originalStates) {
				for(State s2 : originalStates) {
					if(s1 != s2) {
						Set<Proposition> socProps = new HashSet<Proposition>();
						for(Proposition p : s1.getPropositions()) {
							if(socPropsMap.contains(p)) {
								socProps.add(p);
							}
						}
						if(s2.getPropositions().containsAll(socProps)) {
							sm.insertEdge(soc, s1, s2);
						}
					}
				}
			}
		}
	}
}
