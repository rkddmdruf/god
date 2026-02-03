package orm;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SaveList {
	protected static List<Entity>[] saveList = new ArrayList[6];
	
	private static int index = 0;
	protected static <E extends Entity> void addList(List<E> list) {
		saveList[index] = (List<Entity>) list;
		int i = (checkSaveListInClass(list.get(0).getClass()) == -1 ? 0 : 1);
		index += i;
	}
	
	protected static List<Entity> getSaveList(Class<? extends Entity> c) {
		int i = checkSaveListInClass(c);
		if(i == -1) {
			return null;
		}
		return saveList[i];
	}
	
	protected static List<Entity> checkHaveList(Class<? extends Entity> c) {
		int i = checkSaveListInClass(c);
		return i == -1 ? null : saveList[i];
	}
	
	protected static int checkSaveListInClass(Class<? extends Entity> c) {
		for(int i = 0; i < saveList.length; i++) {
			if(saveList[i] != null && saveList[i].get(0).getClass() == c) {
				return i;
			}
		}
		return -1;
	}
}
