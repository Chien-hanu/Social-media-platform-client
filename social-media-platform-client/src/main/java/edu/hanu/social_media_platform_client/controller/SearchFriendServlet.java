package edu.hanu.social_media_platform_client.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.hanu.social_media_platform_client.model.Profile;
import edu.hanu.social_media_platform_client.service.FriendListService;
import edu.hanu.social_media_platform_client.service.ProfileService;

public class SearchFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ProfileService profileService = new ProfileService();
	FriendListService friendListService = new FriendListService();

	public SearchFriendServlet() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String profileName = (String) session.getAttribute("name");
		System.out.println("profile name : " + profileName);
		String searchName = request.getParameter("searchName");
		System.out.println("Searching for : " + searchName);
		List<Profile> notFollowing = notFollowing(searchName, profileName);
		List<Profile> following = following(searchName, profileName);
		for (Profile p : following) {
			System.out.println(p.toString());
		}
		System.out.println("11111111111111");
		for (Profile p : notFollowing) {
			System.out.println(p.toString());
		}
		request.setAttribute("friends", following);
		request.setAttribute("profiles", notFollowing);
		RequestDispatcher dispatcher = request.getRequestDispatcher("/view/user/searchFriend.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

//not follow
	public List<Profile> getProfiles(String searchName) {
		System.out.println("Name search for: " + searchName);
		String[] words = searchName.split("\\s");
		List<Profile> allProfiles = new ArrayList<Profile>();
		List<Profile> filteredProfiles = new ArrayList<Profile>();
		allProfiles = profileService.getAllProfiles();
		for (Profile profile : allProfiles) {
			for (String w : words) {
				if (w.equals(profile.getFirstName()) || w.equals(profile.getLastName())) {
					filteredProfiles.add(profile);
				}
			}
		}
		System.out.println("check 1");
		return filteredProfiles;
	}

	public List<Profile> notFollowing(String searchName, String profileName) {
		List<Profile> allFriends = friendListService.getFriendList(profileName);
		List<Profile> allSearch = getProfiles(searchName);
		List<Profile> notFollowing = new ArrayList<Profile>();
		if (allFriends.size() == 0) {
			return allSearch;
		} else {
			System.out.println("check 2");
			for (Profile p1 : allSearch) {
				for (Profile p2 : allFriends) {
					if (p1.getProfileName().equals(p2.getProfileName())) {
						continue;
					} else {
						notFollowing.add(p1);
					}
				}
			}
		}
		System.out.println("check 3");
		return notFollowing;
	}

	public List<Profile> following(String searchName, String profileName) {
		List<Profile> allFriends = friendListService.getFriendList(profileName);
		List<Profile> allSearch = getProfiles(searchName);
		List<Profile> following = new ArrayList<Profile>();
		System.out.println("check 4");
		if (allFriends.size() == 0) {
			return following;
		} else {
			for (Profile p1 : allSearch) {
				for (Profile p2 : allFriends) {
					if (p1.getProfileName().equals(p2.getProfileName())) {
						following.add(p1);
					} else {
						continue;
					}
				}
			}
		}
		System.out.println("check 5");
		return following;
	}
}
