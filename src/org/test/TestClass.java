package org.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class TestClass {

	@SuppressWarnings("unused")
	private static final String TEST = 
			"Q1|HARD|Tag1"
					+ "\nQ2|EASY|Tag2"
					+ "\nQ3|HARD|Tag3"
					+ "\nQ4|HARD|Tag4"
					+ "\nQ5|HARD|Tag5"
					+ "\nQ6|HARD|Tag6"
					+ "\nQ7|MEDIUM|Tag6"
					+ "\nQ8|MEDIUM|Tag6"
					+ "\nQ9|EASY|Tag6"
					+ "\nQ10|MEDIUM|Tag1"
					+ "\nQ11|HARD|Tag6"
					+ "\nQ12|MEDIUM|Tag5"
					+ "\nQ13|EASY|Tag4"
					+ "\nQ14|MEDIUM|Tag3"
					+ "\nQ15|MEDIUM|Tag2"
					+ "\nQ16|HARD|Tag1"
					+ "\nQ17|MEDIUM|Tag6"
					+ "\nQ18|EASY|Tag5"
					+ "\nQ19|MEDIUM|Tag4"
					+ "\nQ20|MEDIUM|Tag3"
					+ "\nQ21|HARD|Tag2"
					+ "\nQ21|EASY|Tag1";

	private static final String TAG1 = "tag1";
	private static final String TAG2 = "tag2";
	private static final String TAG3 = "tag3";
	private static final String TAG4 = "tag4";
	private static final String TAG5 = "tag5";
	private static final String TAG6 = "tag6";
	private static final String EASY = "EASY";
	private static final String MEDIUM = "MEDIUM";
	private static final String HARD = "HARD";


	public static void main(String[] arg) throws FileNotFoundException {

		if(arg == null || arg.length == 0) {
			System.out.println("Please pass path");
			return;
		}
		File[] files = new File(arg[0]).listFiles();
		if(files == null) {
			System.out.println("No directory");
			return;
		}
		Scanner scan = null;
		String[] inp = null;

		Map<String, QuestionWrapper> questIdVsQuestion = new TreeMap<String, QuestionWrapper>();
		Map<String, List<String>> tagIdVsQuestionId = new HashMap<String, List<String>>();
		Map<String, List<String>> levelVsQuestionId = new HashMap<String, List<String>>();
		Set<String> tag = new HashSet<String>();
		List<String> questInCurrentSet = new ArrayList<>(10);
		List<String> toBeRemoved = new ArrayList<String>(2);
		Map<String, Integer> levelVsCount = new HashMap<String, Integer>();
		for(File file : files) {
			if(!file.getName().endsWith(".txt"))
				continue;
			scan = new Scanner(file);

			if(!questIdVsQuestion.isEmpty())
				questIdVsQuestion.clear();
			if(!tagIdVsQuestionId.isEmpty())
				tagIdVsQuestionId.clear();
			if(!levelVsQuestionId.isEmpty())
				levelVsQuestionId.clear();
			if(!tag.isEmpty())
				tag.clear();
			if(!questInCurrentSet.isEmpty())
				questInCurrentSet.clear();
			if(!toBeRemoved.isEmpty())
				toBeRemoved.clear();
			if(!levelVsCount.isEmpty())
				levelVsCount.clear();

			int count, min = 0;
			String currentTag = null;

			while(scan.hasNextLine()) {
				inp = scan.nextLine().split("\\|");
				questIdVsQuestion.put(inp[0].trim(), new QuestionWrapper(inp[0].trim(), inp[2].trim().toLowerCase(), inp[1].trim().toUpperCase()));

				if(!tagIdVsQuestionId.containsKey(inp[2].trim().toLowerCase()))
					tagIdVsQuestionId.put(inp[2].trim().toLowerCase(), new ArrayList<String>(25));
				tagIdVsQuestionId.get(inp[2].trim().toLowerCase()).add(inp[0].trim());

				if(!levelVsQuestionId.containsKey(inp[1].trim().toUpperCase()))
					levelVsQuestionId.put(inp[1].trim().toUpperCase(), new ArrayList<String>(25));
				levelVsQuestionId.get(inp[1].trim().toUpperCase()).add(inp[0].trim());
			}

			for(String key: tagIdVsQuestionId.keySet()) {
				Collections.sort(tagIdVsQuestionId.get(key));
			}


			for(String key: levelVsQuestionId.keySet()) {
				Collections.sort(levelVsQuestionId.get(key));
			}

			while(true) {
				if(tagIdVsQuestionId.get(TAG1).size() < 1 || tagIdVsQuestionId.get(TAG2).size() < 1 || tagIdVsQuestionId.get(TAG3).size() < 1 
						|| tagIdVsQuestionId.get(TAG4).size() < 1 || tagIdVsQuestionId.get(TAG5).size() < 1 || tagIdVsQuestionId.get(TAG6).size() < 1
						|| levelVsQuestionId.get(EASY).size() < 2 || levelVsQuestionId.get(MEDIUM).size() < 2 || levelVsQuestionId.get(HARD).size() < 2
						|| questIdVsQuestion.size() < 10)
					break;

				//Only two 
				for(String level : levelVsQuestionId.keySet()) {
					count = 0;
					currentTag = null;
					if(!levelVsCount.containsKey(level))
						levelVsCount.put(level, 0);
					for(String key : levelVsQuestionId.get(level)) {

						if(questIdVsQuestion.containsKey(key) && !questInCurrentSet.contains(key) && !tag.contains(questIdVsQuestion.get(key).getTagId())) {
							questInCurrentSet.add(key);
							currentTag = questIdVsQuestion.get(key).getTagId();
							tag.add(currentTag);
							toBeRemoved.add(key);
							questIdVsQuestion.remove(key);
							tagIdVsQuestionId.get(currentTag).remove(key);
							count++;
							levelVsCount.put(level, levelVsCount.get(level)+1);
						}
						if(count >= 2)
							break;
					}
					if(!toBeRemoved.isEmpty()) {
						levelVsQuestionId.get(level).removeAll(toBeRemoved);
						toBeRemoved.clear();
					}
				}

				String level = null;

				if(tag.size() < 6) {
					int total = tagIdVsQuestionId.keySet().size();
					for(String tagNotPresent : tagIdVsQuestionId.keySet()) {
						total--;
						if(tag.size() > 5)
							break;
						if(!tag.contains(tagNotPresent)) {
							level = getLevel(levelVsQuestionId, levelVsCount);
							for(String key : levelVsQuestionId.get(level)) {
								if(questIdVsQuestion.get(key).getTagId().equals(tagNotPresent)) {
									questInCurrentSet.add(key);
									currentTag = questIdVsQuestion.get(key).getTagId();
									tag.add(currentTag);
									toBeRemoved.add(key);
									questIdVsQuestion.remove(key);
									tagIdVsQuestionId.get(currentTag).remove(key);
									levelVsCount.put(level, levelVsCount.get(level)+1);
									break;
								}
							}
							if(!toBeRemoved.isEmpty()) {
								levelVsQuestionId.get(level).removeAll(toBeRemoved);
								toBeRemoved.clear();
							}
						}
					}
					if(total == 0 && tag.size() < 6) {
						for(String tagNotPresent : tagIdVsQuestionId.keySet()) {
							if(tag.size() > 5)
								break;
							if(!tag.contains(tagNotPresent) && tagIdVsQuestionId.get(tagNotPresent).get(0) != null) {
								questInCurrentSet.add(tagIdVsQuestionId.get(tagNotPresent).get(0));
								currentTag = questIdVsQuestion.get(tagIdVsQuestionId.get(tagNotPresent).get(0)).getTagId();
								tag.add(currentTag);
								toBeRemoved.add(tagIdVsQuestionId.get(tagNotPresent).get(0));
								level = questIdVsQuestion.get(tagIdVsQuestionId.get(tagNotPresent).get(0)).getDifficultyLeve();
								levelVsCount.put(level, levelVsCount.get(level)+1);
								tagIdVsQuestionId.get(currentTag).remove(tagIdVsQuestionId.get(tagNotPresent).get(0));
								questIdVsQuestion.remove(tagIdVsQuestionId.get(tagNotPresent).get(0));
							}
						}
					}
				}


				for(String levelNotPresent: levelVsQuestionId.keySet()) {
					if(!levelVsCount.containsKey(levelNotPresent) || levelVsCount.get(levelNotPresent) < 2) {
						for(String questId : tagIdVsQuestionId.get(getTag(tagIdVsQuestionId, tag))) {
							if(questIdVsQuestion.get(questId).getDifficultyLeve().equals(levelNotPresent)) {
								toBeRemoved.add(questId);
								questIdVsQuestion.remove(questId);
								levelVsCount.put(levelNotPresent, levelVsCount.get(levelNotPresent)+1);
								questInCurrentSet.add(questId);
								if(levelVsCount.get(levelNotPresent) > 2)
									break;
							}
						}
						if(!levelVsCount.containsKey(levelNotPresent) || levelVsCount.get(levelNotPresent) < 2) {
							while(levelVsCount.get(levelNotPresent) < 2 && levelVsQuestionId.get(levelNotPresent).get(0) != null) {
								questIdVsQuestion.remove(levelVsQuestionId.get(levelNotPresent).get(0));
								levelVsCount.put(levelNotPresent, levelVsCount.get(levelNotPresent)+1);
								questInCurrentSet.add(levelVsQuestionId.get(levelNotPresent).get(0));
								if(levelVsCount.get(levelNotPresent) > 2)
									break;
							}
						}
					}
				}

				if(questInCurrentSet.size() < 10) {

					Iterator<String> questIterator = questIdVsQuestion.keySet().iterator();
					for(int ind = 0; ind < questIdVsQuestion.keySet().size(); ind++) {
						for(String questId: levelVsQuestionId.get(getLevel(levelVsQuestionId, levelVsCount))) {
							if(tagIdVsQuestionId.get(getTag(tagIdVsQuestionId, tag)).contains(questId) 
									&& questIdVsQuestion.containsKey(questId) && !questInCurrentSet.contains(questId)) {
								questInCurrentSet.add(questId);
								toBeRemoved.add(questId);
								questIdVsQuestion.remove(questId);
								questIterator = questIdVsQuestion.keySet().iterator();
								tagIdVsQuestionId.get(getTag(tagIdVsQuestionId, tag)).remove(questId);
								break;
							}
						}
						if(!toBeRemoved.isEmpty()) {
							levelVsQuestionId.get(getLevel(levelVsQuestionId, levelVsCount)).removeAll(toBeRemoved);
							toBeRemoved.clear();
						}
						if(tagIdVsQuestionId.get(TAG1).size() < 1 || tagIdVsQuestionId.get(TAG2).size() < 1 || tagIdVsQuestionId.get(TAG3).size() < 1 
								|| tagIdVsQuestionId.get(TAG4).size() < 1 || tagIdVsQuestionId.get(TAG5).size() < 1 || tagIdVsQuestionId.get(TAG6).size() < 1
								|| levelVsQuestionId.get(EASY).size() < 2 || levelVsQuestionId.get(MEDIUM).size() < 2 || levelVsQuestionId.get(HARD).size() < 2)
							break;

					}
					if(questInCurrentSet.size() < 10) {
						String ques = null;
						while(questIterator.hasNext()) {
							ques = questIterator.next();
							questInCurrentSet.add(ques);
							toBeRemoved.add(ques);
							tagIdVsQuestionId.get(questIdVsQuestion.get(ques).getTagId()).remove(ques);
							levelVsQuestionId.get(questIdVsQuestion.get(ques).getDifficultyLeve()).remove(ques);
							questIdVsQuestion.remove(ques);
							questIterator = questIdVsQuestion.keySet().iterator();
							if(questInCurrentSet.size() >= 10)
								break;
						}

					}
				}
				
				

				if(questInCurrentSet.size() == 10) {
					min++;
					//System.out.println(questInCurrentSet+"\n"+tag+"\n"+levelVsCount);
					questInCurrentSet.clear();
				}
				else if(questInCurrentSet.size() < 10) {
					break;
				}

			}
			System.out.println(file.getName()+" -> "+min);
			scan.close();

		}
	}

	private static String getLevel(Map<String, List<String>> levelVsQuestionId, Map<String, Integer> levelVsCunt) {
		for(String level : levelVsCunt.keySet()) {
			if(!levelVsCunt.containsKey(level))
				return level;
			if(levelVsCunt.get(level) < 2)
				return level;
		}
		if(levelVsQuestionId.get(EASY).size() - levelVsQuestionId.get(MEDIUM).size() > 0 
				&& levelVsQuestionId.get(EASY).size() - levelVsQuestionId.get(HARD).size() > 0)
			return EASY;
		if(levelVsQuestionId.get(MEDIUM).size() - levelVsQuestionId.get(EASY).size() > 0 
				&& levelVsQuestionId.get(MEDIUM).size() - levelVsQuestionId.get(HARD).size() > 0)
			return MEDIUM;
		if(levelVsQuestionId.get(HARD).size() - levelVsQuestionId.get(MEDIUM).size() > 0 
				&& levelVsQuestionId.get(HARD).size() - levelVsQuestionId.get(EASY).size() > 0)
			return HARD;
		return EASY;
	}


	private static String getTag(Map<String, List<String>> tagIdVsQuestionId, Set<String> tag)  {

		for(String key : tagIdVsQuestionId.keySet()) {
			if(!tag.contains(key))
				return key;
		}

		int tag1 = tagIdVsQuestionId.get(TAG1).size();
		int tag2 = tagIdVsQuestionId.get(TAG2).size();
		int tag3 = tagIdVsQuestionId.get(TAG3).size();
		int tag4 = tagIdVsQuestionId.get(TAG4).size();
		int tag5 = tagIdVsQuestionId.get(TAG5).size();
		int tag6 = tagIdVsQuestionId.get(TAG6).size();

		if(tag1 > 1 && tag1 > tag2 && tag1 > tag3 && tag1 > tag4 && tag1 > tag5 && tag1 > tag6)
			return TAG1;
		if(tag2 > 1 && tag2 > tag1 && tag2 > tag3 && tag2 > tag4 && tag2 > tag5 && tag2 > tag6)
			return TAG2;
		if(tag3 > 1 && tag3 > tag2 && tag3 > tag1 && tag3 > tag4 && tag3 > tag5 && tag3 > tag6)
			return TAG3;
		if(tag4 > 1 && tag4 > tag2 && tag4 > tag3 && tag4 > tag1 && tag4 > tag5 && tag4 > tag6)
			return TAG4;
		if(tag5 > 1 && tag5 > tag2 && tag5 > tag3 && tag5 > tag4 && tag5 > tag1 && tag5 > tag6)
			return TAG5;
		if(tag6 > 1 && tag6 > tag2 && tag6 > tag3 && tag6 > tag4 && tag6 > tag5 && tag6 > tag1)
			return TAG6;
		return TAG1;
	}

}
