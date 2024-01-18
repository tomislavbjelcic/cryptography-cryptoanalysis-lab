package kik.lab2;

import java.security.Key;
import java.util.List;
import java.util.Map;

public interface KeyExtractor {
	
	
	Key extract(Map<String, List<String>> data);
	
	int getKeySize();
	String getAlgorithm();
	
}
