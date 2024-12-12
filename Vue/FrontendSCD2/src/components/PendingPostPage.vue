<template>
    <div>
      <h1>Pending Posts</h1>
      <div class="topRowButtons">
      <button @click="navigateToMainPage" class="navigate-button">Main Page</button>
      <button @click="logout" class="logout-button">Logout</button>
    </div>
      <!-- Postări în așteptare -->
      <div v-for="post in pendingPosts" :key="post.id" class="post-card">
        <h2>{{ post.title }}</h2>
        <p>{{ post.content }}</p>
        <p><strong>Author:</strong> {{ post.userName || "Unknown" }}</p>
        <p><strong>Date:</strong> {{ formatDate(post.createdOn) }}</p>
  
        <!-- Buton pentru comentarii -->
        <!-- <button @click="toggleComments(post.id)" class="comment-toggle-button">
          {{ showComments[post.id] ? "Hide Comments" : "Show Comments" }}
        </button> -->
  
        <!-- Secțiunea pentru comentarii -->
        <!-- <div v-if="showComments[post.id]" class="comments-section">
          <div v-if="post.comments && post.comments.length > 0" v-for="comment in post.comments" :key="comment.id">
            <p><strong>{{ comment.userName || "Unknown" }}:</strong> {{ comment.content }}</p>
          </div>
          <p v-else>No comments yet.</p>
        </div> -->
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        pendingPosts: [],
        showComments: {}, // Obiect pentru a ține evidența comentariilor afișate
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
      async fetchPendingPosts() {
        try {
          const response = await axios.get("http://localhost:8083/getAllPosts");
          this.pendingPosts = response.data
            .filter((post) => post.status === "PENDING")
            .sort((a, b) => new Date(b.createdOn) - new Date(a.createdOn)); // Sortează în ordine descrescătoare
        } catch (error) {
          console.error("Error fetching pending posts:", error);
        }
      },
      formatDate(date) {
        if (!date) return "Invalid Date";
        return new Date(date).toLocaleDateString();
      },
    },
    mounted() {
      this.fetchPendingPosts();
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
  .create-button, .navigate-button {
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
  
  
  /* .comment-button {
    background-color: #d35400;
    color: white;
    padding: 5px 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
  } */
  
  .create-button:hover, .navigate-button:hover {
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