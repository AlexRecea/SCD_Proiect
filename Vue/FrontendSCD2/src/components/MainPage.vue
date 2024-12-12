<template>
  <div>
    <h1>Published Posts</h1>
    <div class="topRowButtons">
      <button @click="openCreatePostModal" class="create-button">Create Post</button>
      <button @click="navigateToPendingPosts" class="navigate-button">Show Pending Posts</button>
      <button @click="navigateToUserPosts" class="navigate-button">My Posts</button>
      <button @click="logout" class="logout-button">Logout</button>
    </div>
    

    <!-- Modal pentru crearea unei postări -->
    <div v-if="showModal" class="modal">
      <div class="modal-content">
        <h3>Create a New Post</h3>
        <form @submit.prevent="createPost">
          <div class="form-group">
            <label for="title">Title:</label>
            <input
              type="text"
              id="title"
              v-model="post.title"
              required
              placeholder="Enter the post title"
              class="input-field"
            />
          </div>
          <div class="form-group">
            <label for="content">Content:</label>
            <textarea
              id="content"
              v-model="post.content"
              required
              placeholder="Enter the post content"
              class="textarea-field"
            ></textarea>
          </div>
          <div class="button-group">
            <button type="submit" class="submit-button">Submit</button>
            <button type="button" @click="closeCreatePostModal" class="cancel-button">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal pentru adăugarea unui comentariu -->
    <div v-if="showCommentModal" class="modal">
      <div class="modal-content">
        <h3>Add a Comment</h3>
        <form @submit.prevent="addComment">
          <div class="form-group">
            <!-- <label for="comment-content">Comment:</label> -->
            <textarea
              id="comment-content"
              v-model="newComment.content"
              required
              placeholder="Enter your comment"
              class="textarea-field"
            ></textarea>
          </div>
          <div class="button-group">
            <button type="submit" class="submit-button">Submit</button>
            <button type="button" @click="closeCommentModal" class="cancel-button">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Postări publicate -->
    <div v-for="post in publishedPosts" :key="post.id" class="post-card">
      <h2>{{ post.title }}</h2>
      <p>{{ post.content }}</p>
      <p><strong>Author:</strong> {{ post.userName || "Unknown" }}</p>
      <p><strong>Date:</strong> {{ formatDate(post.createdOn) }}</p>

      <!-- Buton pentru comentarii -->
      <button @click="toggleComments(post.id)" class="comment-toggle-button">
        {{ showComments[post.id] ? "Hide Comments" : "Show Comments" }}
      </button>

      <!-- Secțiunea pentru comentarii -->
      <div v-if="showComments[post.id]" class="comments-section">
        <div v-if="post.comments && post.comments.length > 0" v-for="comment in post.comments" :key="comment.id">
          <p><strong>{{ comment.userName || "Unknown" }}:</strong> {{ comment.content }}</p>
        </div>
        <p v-else>No comments yet.</p>
      </div>

      <!-- Buton pentru adăugarea unui comentariu -->
      <button @click="openCommentModal(post.id)" class="comment-button">Add Comment</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      posts: [],
      currentUser: JSON.parse(localStorage.getItem("currentUser")),
      showModal: false,
      showCommentModal: false, // Pentru controlul popup-ului de comentarii
      post: {
        title: "",
        content: "",
      },
      newComment: {
        postId: null, // ID-ul postării curente pentru comentarii
        content: "",
      },
      publishedPosts: [],
      showComments: {}, // Obiect pentru a ține evidența postărilor pentru care sunt afișate comentariile
    };
  },
  methods: {
    // Deschide modalul pentru crearea postărilor
    openCreatePostModal() {
      this.showModal = true;
      this.showCommentModal = false; // Închide modalul de comentarii, dacă e deschis
    },
    closeCreatePostModal() {
      this.showModal = false;
    },
    // Deschide modalul pentru comentarii
    openCommentModal(postId) {
      this.showCommentModal = true;
      this.showModal = false; // Închide modalul de creare postare, dacă e deschis
      this.newComment = {
        postId,
        content: "",
      };
    },
    closeCommentModal() {
      this.showCommentModal = false;
    },

    navigateToPendingPosts() {
      this.$router.push("/pending");
    },
    navigateToUserPosts() {
      this.$router.push("/userposts");
    },
    logout() {
    // Elimină utilizatorul curent din localStorage
    localStorage.removeItem("currentUser");
    // Redirecționează utilizatorul către pagina de login
    this.$router.push("/");
  },
    async createPost() {
      try {
        const payload = {
          title: this.post.title,
          content: this.post.content,
          user: {
            id: this.currentUser.id,
          },
        };

        const response = await axios.post("http://localhost:8083/createPost", payload);

        if (response.status === 201 || response.status === 200) {
          alert("Post created successfully!");
          this.closeCreatePostModal();
          this.fetchPublishedPosts();
        } else {
          alert("Failed to create the post. Please try again.");
        }
      } catch (error) {
        console.error("Error creating post:", error);
        alert("An error occurred while creating the post.");
      }
    },
    async addComment() {
      try {
        const payload = {
          content: this.newComment.content,
          postId: this.newComment.postId,
          userId: this.currentUser.id
        };

        const response = await axios.post("http://localhost:8083/addComment", payload);

        if (response.status === 201 || response.status === 200) {
          // alert("Comment added successfully!");
          this.closeCommentModal();
          this.fetchPublishedPosts();
        } else {
          alert("Failed to add the comment. Please try again.");
        }
      } catch (error) {
        console.error("Error adding comment:", error);
        alert("An error occurred while adding the comment.");
      }
    },

    async fetchPublishedPosts() {
      try {
    const response = await axios.get("http://localhost:8083/getAllPosts");
    this.publishedPosts = response.data
      .filter((post) => post.status === "PUBLISHED")
          .sort((a, b) => new Date(b.createdOn) - new Date(a.createdOn)); // Sortează în ordine descrescătoare

    
  } catch (error) {
    console.error("Error fetching posts:", error);
  }
    },
    formatDate(date) {
      if (!date) return "Invalid Date";
      return new Date(date).toLocaleDateString();
    },
    toggleComments(postId) {
      this.showComments = {
        ...this.showComments,
        [postId]: !this.showComments[postId],
      };
    },
  },
  mounted() {
    this.fetchPublishedPosts();
  },
};
</script>

<style>
/* Stil pentru întreaga pagină */
body {
  font-family:'Courier New', Courier, monospace, sans-serif;
  background-color: #fffaf5;
  margin: 0;
  padding: 0;
  color: #5a3e2f;
  /* background-color: beige; */
}

h1 {
  text-align: center;
  color: #b55d2c;
  margin-top: 20px;
}

/* Stil pentru butonul de creare */
.create-button, .navigate-button, .logout-button {
  display: block;
  margin: 20px auto;
  padding: 10px 20px;
  background-color: #b55d2c;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.topRowButtons {
  display: flex;
  justify-content: space-around;
}

.comment-button {
  background-color: #d35400;
  color: white;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.create-button:hover, .navigate-button:hover, .logout-button:hover {
  background-color: #933f1e;
}

/* Stil pentru popup */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: #fff8ec;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 6px 10px rgba(0, 0, 0, 0.15);
  width: 400px;
  max-width: 80%;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #b55d2c;
}

.form-group {
  margin-bottom: 15px;
  align-self: center;
}

.input-field,
.textarea-field {
  width: 90%;
  padding: 10px;
  border: 1px solid #b55d2c;
  border-radius: 5px;
  font-size: 14px;
}

.textarea-field {
  resize: none;
  height: 100px;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.submit-button {
  padding: 10px 20px;
  background-color: #b55d2c;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #933f1e;
}

.cancel-button {
  padding: 10px 20px;
  background-color: #ccc;
  color: #5a3e2f;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-button:hover {
  background-color: #bbb;
}

/* Stil pentru cardurile de postări */
.post-card {
  display: flex;
  flex-direction: column;
  align-items: center;
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
.comment-toggle-button {
  background-color: #b55d2c;
  color: white;
  padding: 5px 15px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  margin-top: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

.comment-toggle-button:hover {
  background-color: #933f1e;
}

</style>