import requests

BASE_URL = "http://localhost:8083"

def get_posts():
    try:
        response = requests.get(f"{BASE_URL}/getAllPosts")
        if response.status_code == 200:
            return response.json()
        else:
            print(f"Error fetching posts: {response.status_code}")
            return None
    except Exception as e:
        print(f"Exception occurred: {e}")
        return None

def update_post_status(post_id, status):
    try:
        data = {"status": status}
        response = requests.put(f"{BASE_URL}/updatePost/{post_id}", json=data)
        if response.status_code == 200:
            return True
        else:
            print(f"Error updating post status: {response.status_code}")
            return False
    except Exception as e:
        print(f"Exception occurred: {e}")
        return False

def delete_post(post_id):
    try:
        response = requests.delete(f"{BASE_URL}/deletePost/{post_id}")
        if response.status_code == 204:
            return True
        else:
            print(f"Error deleting post: {response.status_code}")
            return False
    except Exception as e:
        print(f"Exception occurred: {e}")
        return False