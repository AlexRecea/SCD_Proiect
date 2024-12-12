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

.topRowButtons {
  display: flex;
  justify-content: space-around;
}
</style>