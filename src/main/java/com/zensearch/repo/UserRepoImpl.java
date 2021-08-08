package com.zensearch.repo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zensearch.pojo.User;

public class UserRepoImpl implements UserRepoIfc {
	
	private List<User> masterUserList = new ArrayList<User>();
	
	
	private void loadUserData() {
		InputStreamReader userReader = null;
		InputStream userInputStream = null;
		try {			
			userInputStream = UserRepoImpl.class.getClassLoader().getResourceAsStream(userFileName);
			userReader = new InputStreamReader(userInputStream,encoding);
			masterUserList = new Gson().fromJson(userReader, new TypeToken<List<User>>() {}.getType());
			userReader.close();
			
			/** make the list unmodifiable **/
			Collections.unmodifiableList(masterUserList);

		} catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			IOUtils.closeQuietly(userInputStream);
		}
	}
	
	public List<User> getAllUsers(){
		/** if data has not been loaded then load , otherwise simply return loaded list of Users **/
		if(null == masterUserList || masterUserList.size() <= 0) {
			loadUserData();
		}
		return masterUserList;
	}
}

