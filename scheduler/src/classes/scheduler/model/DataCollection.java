package scheduler.model;

import java.util.ArrayList;
import java.util.List;

public class DataCollection {
	public static List<SequenceMessage> sequenceMessages = new ArrayList<SequenceMessage>();
	
	public static List<SequenceMessage> getAllPassedSequences() {
		List<SequenceMessage> result = new ArrayList<SequenceMessage>();
		for (SequenceMessage sm : sequenceMessages) {
			if (sm.isSuccess) {
				result.add(sm);
			}
		}
		return result;
	}
	
	public static List<SequenceMessage> getAllFailedSequences() {
		List<SequenceMessage> result = new ArrayList<SequenceMessage>();
		for (SequenceMessage sm : sequenceMessages) {
			if (!sm.isSuccess) {
				result.add(sm);
			}
		}
		return result;
	}
	
	public static List<Pattern> getAllPatterns() {
		List<Pattern> result = new ArrayList<Pattern>();
		List<Pattern> interleavings = Pattern.getInterleavings();
		
		for (SequenceMessage sm : sequenceMessages) {
			sm.removeDeprecatedRWNodes();
			List<Pattern> patterns = sm.containInterleavings(interleavings);
			result.addAll(patterns);
		}
		result = DataCollection.removeRepeatePatterns(result);
		return result;
	}
	
	private static List<Pattern> removeRepeatePatterns(List<Pattern> patterns) {
		List<Pattern> result = new ArrayList<Pattern>();
		nextPattern:
		for (Pattern pattern : patterns) {
			for (int i = 0; i < result.size(); i++) {
				if (pattern.equals(result.get(i))) {
					continue nextPattern;
				}
			}
			result.add(pattern);
		}
		return result;
	}
}
