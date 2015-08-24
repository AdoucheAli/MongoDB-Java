/*
 * Copyright (c) 2008 - 2013 10gen, Inc. <http://10gen.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package course;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BlogPostDAO {
	DBCollection postsCollection;

	public BlogPostDAO(final DB blogDatabase) {
		postsCollection = blogDatabase.getCollection("posts");
	}

	// Return a single post corresponding to a permalink
	@SuppressWarnings("unchecked")
	public DBObject findByPermalink(String permalink) {

		DBObject post = postsCollection.findOne(new BasicDBObject("permalink",
				permalink));

		// fix up if a post has no likes
		if (post != null) {
			List<DBObject> comments = (List<DBObject>) post.get("comments");
			for (DBObject comment : comments) {
				if (!comment.containsField("num_likes")) {
					comment.put("num_likes", 0);
				}
			}
		}
		return post;
	}

	// Return a list of posts in descending order. Limit determines
	// how many posts are returned.
	public List<DBObject> findByDateDescending(int limit) {

		List<DBObject> posts = null;
		// Return a list of DBObjects, each one a post from the posts collection
		DBCursor cursor = postsCollection.find()
				.sort(new BasicDBObject("date", -1)).limit(limit);

		try {
			if (cursor != null) {
				posts = cursor.toArray();
			}
		} finally {
			cursor.close();
		}
		return posts;
	}

	/**
	 * Find by tag date descending.
	 * 
	 * @param tag
	 *            the tag
	 * @return the list
	 */
	public List<DBObject> findByTagDateDescending(final String tag) {
		List<DBObject> posts;
		BasicDBObject query = new BasicDBObject("tags", tag);
		System.out.println("/tag query: " + query.toString());
		DBCursor cursor = postsCollection.find(query)
				.sort(new BasicDBObject().append("date", -1)).limit(10);
		try {
			posts = cursor.toArray();
		} finally {
			cursor.close();
		}
		return posts;
	}

	@SuppressWarnings("rawtypes")
	public String addPost(String title, String body, List tags, String username) {

		System.out.println("inserting blog entry " + title + " " + body);

		String permalink = title.replaceAll("\\s", "_"); // whitespace becomes _
		permalink = permalink.replaceAll("\\W", ""); // get rid of non
														// alphanumeric
		permalink = permalink.toLowerCase();

		BasicDBObject post = new BasicDBObject();
		// Remember that a valid post has the following keys:
		// author, body, permalink, tags, comments, date
		//
		// A few hints:
		// - Don't forget to create an empty list of comments
		// - for the value of the date key, today's datetime is fine.
		// - tags are already in list form that implements suitable interface.
		// - we created the permalink for you above.

		List<DBObject> commentList = new ArrayList<DBObject>();

		// Build the post object and insert it
		post.append("title", title).append("author", username)
				.append("body", body).append("permalink", permalink)
				.append("tags", tags).append("date", new Date())
				.append("comments", commentList);

		try {
			postsCollection.insert(post);
			System.out.println("Inserting blog post with permalink "
					+ permalink);
		} catch (Exception e) {
			System.out.println("Error inserting post");
			return null;
		}

		return permalink;
	}

	// White space to protect the innocent

	// Append a comment to a blog post
	public void addPostComment(final String name, final String email,
			final String body, final String permalink) {

		// Hints:
		// - email is optional and may come in NULL. Check for that.
		// - best solution uses an update command to the database and a suitable
		// operator to append the comment on to any existing list of comments
		DBObject existingPost = this.findByPermalink(permalink);

		DBObject commentObject = new BasicDBObject("author", name).append(
				"body", body);
		// Add email if present
		if (email != null && !email.isEmpty()) {
			commentObject.put("email", email);
		}
		// Update the existing post to push the comment in
		this.postsCollection.update(existingPost, new BasicDBObject("$push",
				new BasicDBObject("comments", commentObject)), false, false);
	}

	/**
	 * Like post.
	 * 
	 * @param permalink
	 *            the permalink
	 * @param ordinal
	 *            the ordinal
	 */
	@SuppressWarnings("unchecked")
	public void likePost(final String permalink, final int ordinal) {

		// XXX Final Exam, Please work here
		// Add code to increment the num_likes for the 'ordinal' comment
		// that was clicked on.
		// provided you use num_likes as your key name, no other changes should
		// be required
		// alternatively, you can use whatever you like but will need to make a
		// couple of other
		// changes to templates and post retrieval code.
		DBObject currentPost = this.findByPermalink(permalink);
		// fix up if a post has no likes
		if (currentPost != null) {
			List<DBObject> comments = (List<DBObject>) currentPost
					.get("comments");
			int i = 0;
			for (DBObject comment : comments) {
				if (i == ordinal) {
					DBObject query = new BasicDBObject("permalink", permalink)
							.append("comments.author", comment.get("author"));
					if (comment.containsField("email")) {
						query.put("comments.email", comment.get("email"));
					}
					postsCollection.update(query, new BasicDBObject("$inc",
							new BasicDBObject("comments.$.num_likes", +1)));
					break;
				} else {
					i++;
				}
			}
		}
	}
}
