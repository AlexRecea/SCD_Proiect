<template>
    <div>
      <h1>My Published Posts</h1>
      <div class="topRowButtons">
        <button @click="navigateToMainPage" class="navigate-button">Main Page</button>
        <button @click="logout" class="logout-button">Logout</button>
      </div>
      <div v-if="posts.length === 0" class="no-posts-message">
        <p>You have no published posts.</p>
      </div>
      <div v-else>
        <div v-for="post in posts" :key="post.id" class="post-card">
          <h2>{{ post.title }}</h2>
          <p>{{ post.content }}</p>
          <p><strong>Date:</strong> {{ formatDate(post.createdOn) }}</p>
          <div class="button-group">
          <button @click="deletePost(post.id)" class="navigate-button">Delete Post</button>
          <button @click="openUpdateModal(post)" class="navigate-button">Update Post</button>
        </div>
        </div>
      </div>

          <!-- Modal pentru actualizarea postării -->
    <div v-if="showUpdateModal" class="modal">
      <div class="modal-content">
        <h3>Update Post</h3>
        <form @submit.prevent="updatePost">
          <div class="form-group">
            <label for="update-title">Title:</label>
            <input
              type="text"
              id="update-title"
              v-model="updatedPost.title"
              required
              class="input-field"
            />
          </div>
          <div class="form-group">
            <label for="update-content">Content:</label>
            <textarea
              id="update-content"
              v-model="updatedPost.content"
              required
              class="textarea-field"
            ></textarea>
          </div>
          <div class="button-group">
            <button type="submit" class="submit-button">Save</button>
            <button type="button" @click="closeUpdateModal" class="cancel-button">Cancel</button>
          </div>
        </form>
      </div>
    </div>
    </div>
  </template>
  
  <script>
import axios from "axios";

export default {
  data() {
    return {
      posts: [],
      currentUser: JSON.parse(localStorage.getItem("currentUser")), // Obține utilizatorul curent din localStorage
      showUpdateModal: false, // Control pentru popup-ul de actualizare
      updatedPost: {
        id: null,
        title: "",
        content: "",
      },
    };
  },
    methods: {
        // Navigare înapoi la pagina principală
        navigateToMainPage() {
          this.$router.push("/main");
        },
        // Funcție pentru deconectare
        logout() {
          localStorage.removeItem("currentUser");
          this.$router.push("/");
        },
    async fetchMyPosts() {
      try {
        // API-ul actualizat pentru a obține postările utilizatorului curent
        const response = await axios.get("http://localhost:8083/getMyPosts", {
          params: { userId: this.currentUser.id },
        });

          this.posts = response.data
        .sort((a, b) => new Date(b.createdOn) - new Date(a.createdOn)); // Sortează în ordine descrescătoare
      } catch (error) {
        console.error("Error fetching my posts:", error);
        this.posts = [];
      }
    },
    formatDate(date) {
      if (!date) return "Invalid Date";
      return new Date(date).toLocaleDateString();
        },
    async deletePost(postId) {
      if (confirm("Are you sure you want to delete this post?")) {
        try {
          // API pentru ștergerea unei postări
          await axios.delete(`http://localhost:8083/deletePost/${postId}`);
          alert("Post deleted successfully!");
          // Actualizează lista postărilor după ștergere
          this.fetchMyPosts();
        } catch (error) {
          console.error("Error deleting post:", error);
          alert("An error occurred while deleting the post.");
        }
      }
        },
        openUpdateModal(post) {
      this.updatedPost = { ...post }; // Pre-populează datele postării curente
      this.showUpdateModal = true;
    },
    closeUpdateModal() {
      this.showUpdateModal = false;
    },
    async updatePost() {
      try {
        const payload = {
          id: this.updatedPost.id,
          title: this.updatedPost.title,
          content: this.updatedPost.content,
        };

        const response = await axios.put("http://localhost:8083/updatePost", payload);

        if (response.status === 200 || response.status === 204) {
          alert("Post updated successfully!");
          this.closeUpdateModal();
          this.fetchMyPosts();
        } else {
          alert("Failed to update the post. Please try again.");
        }
      } catch (error) {
        console.error("Error updating post:", error);
        alert("An error occurred while updating the post.");
      }
    },
    },
  mounted() {
    // Apelează funcția pentru a încărca postările utilizatorului la montare
    this.fetchMyPosts();
    },
};
</script>
  
<style>
h1 {
  text-align: center;
  color: #b55d2c;
  margin-top: 20px;
}

.no-posts-message {
  text-align: center;
  margin-top: 20px;
  font-size: 18px;
  color: #5a3e2f;
}

.post-card {
  background-color: #ffe4cc;
  margin: 20px auto;
  padding: 20px;
  border-radius: 10px;
  width: 50%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.post-card h2 {
  margin-top: 0;
  color: #b55d2c;
}

.topRowButtons, .button-group {
  display: flex;
  justify-content: space-around;
}
</style>