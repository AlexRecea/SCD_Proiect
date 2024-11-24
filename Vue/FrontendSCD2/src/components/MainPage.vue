<template>
  <div>
    <h1>Published Posts</h1>
    <button @click="showModal = true" class="create-button">Create Post</button>

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
            <button type="button" @click="showModal = false" class="cancel-button">Cancel</button>
          </div>
        </form>
      </div>
    </div>

    <div v-for="post in publishedPosts" :key="post.id" class="post-card">
      <h2>{{ post.title }}</h2>
      <p>{{ post.content }}</p>
      <p><strong>Author:</strong> {{ post.userName || "Unknown" }}</p>
      <p><strong>Date:</strong> {{ formatDate(post.createdOn) }}</p>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      showModal: false,
      post: {
        title: "",
        content: "",
      },
      publishedPosts: [],
    };
  },
  methods: {
    async createPost() {
      try {
        const payload = {
          title: this.post.title,
          content: this.post.content,
          user: {
            id: 1, // ID-ul utilizatorului logat, ajustat conform aplicației
          },
        };

        const response = await axios.post("http://localhost:8083/createPost", payload);

        if (response.status === 201 || response.status === 200) {
          alert("Post created successfully!");
          this.showModal = false;
          this.post.title = "";
          this.post.content = "";
          this.fetchPublishedPosts();
        } else {
          alert("Failed to create the post. Please try again.");
        }
      } catch (error) {
        console.error("Error creating post:", error);
        alert("An error occurred while creating the post.");
      }
    },
    async fetchPublishedPosts() {
      try {
        const response = await axios.get("http://localhost:8083/getAllPosts");
        this.publishedPosts = response.data.filter((post) => post.status === "PUBLISHED");
      } catch (error) {
        console.error("Error fetching posts:", error);
      }
    },
    formatDate(date) {
      if (!date) return "Invalid Date";
      return new Date(date).toLocaleDateString();
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
  font-family: "Arial", sans-serif;
  background-color: #fffaf5;
  margin: 0;
  padding: 0;
  color: #5a3e2f;
}

h1 {
  text-align: center;
  color: #b55d2c;
  margin-top: 20px;
}

/* Stil pentru butonul de creare */
.create-button {
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

.create-button:hover {
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
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 20px;
  color: #b55d2c;
}

.form-group {
  margin-bottom: 15px;
}

.input-field,
.textarea-field {
  width: 100%;
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
  background-color: #ffe4cc;
  margin: 20px auto;
  padding: 20px;
  border-radius: 10px;
  width: 80%;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.post-card h2 {
  margin-top: 0;
  color: #b55d2c;
}
</style>