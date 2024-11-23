import requests

BASE_URL = "http://localhost:8083"

def login_user(name: str, email: str):
    try:
        response = requests.post(f"{BASE_URL}/api/users/login", json={"name": name, "email": email})
        if response.status_code == 200:
            return response.json()  # Returnează datele utilizatorului
        elif response.status_code == 401:
            return {"error": "Invalid credentials"}
        else:
            return {"error": f"Unexpected error: {response.status_code}"}
    except requests.RequestException as e:
        return {"error": f"Request failed: {e}"}

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