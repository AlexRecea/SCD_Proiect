import requests
import tkinter as tk
from tkinter import Tk, ttk, messagebox, Label, Entry, Button
from datetime import datetime
# URL-ul de bază al serverului
BASE_URL = "http://localhost:8083"

# Variabile globale pentru utilizatorul logat
current_user_name = None
current_user_email = None



# Funcția pentru a verifica dacă utilizatorul există prin endpoint-ul /login
def login_user(name, email):
    try:
        payload = {"name": name, "email": email}
        response = requests.post(f"{BASE_URL}/login", json=payload)
        if response.status_code == 200:
            return True
        elif response.status_code == 401:
            messagebox.showerror("Error", "Invalid name or email.")
            return False
        else:
            messagebox.showerror("Error", f"Login failed with status code {response.status_code}.")
            return False
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        return False

# Funcția pentru a deschide pagina principală după login
def open_main_page():
    global root, current_user_name, current_user_email, tree

    # Distruge fereastra curentă
    for widget in root.winfo_children():
        widget.destroy()

    # Configurarea interfeței principale
    root.title(f"Post Management - Logged in as Admin")
    root.geometry("1200x700")

    # Crearea tabelului (Treeview)
    columns = ("ID", "Title", "Content", "Status", "User", "Date")
    tree = ttk.Treeview(root, columns=columns, show="headings")

    for col in columns:
        tree.heading(col, text=col)
        tree.column(col, width=150)

    tree.pack(fill="both", expand=True)

    # Crearea butoanelor
    button_frame = ttk.Frame(root)
    button_frame.pack(fill="x", padx=10, pady=10)

    accept_button = ttk.Button(button_frame, text="Accept", command=on_accept_button_click)
    accept_button.pack(side="left", padx=5)

    remove_button = ttk.Button(button_frame, text="Remove", command=on_remove_button_click)
    remove_button.pack(side="left", padx=5)

    refresh_button = ttk.Button(button_frame, text="Refresh", command=refresh_posts)
    refresh_button.pack(side="left", padx=5)

    show_comments_button = ttk.Button(button_frame, text="Show Comments", command=open_comments_popup)
    show_comments_button.pack(side="left", padx=5)

    delete_post_button = ttk.Button(button_frame, text="Delete Post", command=delete_post)
    delete_post_button.pack(side="left", padx=5)

    # Încărcarea postărilor la pornire
    refresh_posts()
# Funcția pentru a încărca toate postările
def get_all_posts():
    try:
        response = requests.get(f"{BASE_URL}/getAllPosts")
        if response.status_code == 200:
            return response.json()
        else:
            messagebox.showerror("Error", f"Failed to fetch posts: {response.status_code}")
            return []
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        return []

# Funcția pentru a accepta o postare (modifică statusul în "PUBLISHED")
def accept_post(post_id):
    url = f"{BASE_URL}/updateStatus/{post_id}"
    data = "PUBLISHED"
    headers = {"Content-Type": "text/plain"}

    try:
        response = requests.put(url, data=data, headers=headers)
        if response.status_code == 200:
            messagebox.showinfo("Success", "Post accepted successfully.")
        else:
            messagebox.showerror("Error", "Failed to accept the post.")
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")

# Funcția pentru a elimina o postare (modifică statusul în "REMOVED")
def remove_post(post_id):
    url = f"{BASE_URL}/updateStatus/{post_id}"
    data = "REMOVED"
    headers = {"Content-Type": "text/plain"}

    try:
        response = requests.put(url, data=data, headers=headers)
        if response.status_code == 200:
            messagebox.showinfo("Success", "Post removed successfully (status set to REMOVED).")
        else:
            messagebox.showerror("Error", "Failed to remove the post.")
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")

# Funcția pentru a șterge o postare
def delete_post_from_server(post_id):
    url = f"{BASE_URL}/deletePost/{post_id}"
    try:
        response = requests.delete(url)
        if response.status_code == 204:
            messagebox.showinfo("Success", "Post deleted successfully.")
            return True
        elif response.status_code == 404:
            messagebox.showerror("Error", "Post not found.")
            return False
        else:
            messagebox.showerror("Error", f"Failed to delete post. Status code: {response.status_code}")
            return False
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        return False

# Funcția pentru a încărca postările în tabel
def refresh_posts():
    global tree
    # Șterge toate rândurile existente din tabel
    for row in tree.get_children():
        tree.delete(row)

    # Obține lista de postări
    posts = get_all_posts()

    # Adaugă fiecare postare în tabel
    for post in posts:
        # Accesează userName și createdOn din JSON
        user_name = post.get("userName", "UNKNOWN")
        created_date = post.get("createdOn", "UNKNOWN")
        
        # Formatează data dacă există
        if created_date != "UNKNOWN":
            try:
                created_date = datetime.fromisoformat(created_date).strftime("%d-%m-%Y %H:%M:%S")
            except ValueError:
                created_date = "INVALID DATE"

        # Adaugă rândul în tabel
        tree.insert("", "end", values=(
            post["id"],
            post["title"],
            post["content"],
            post["status"],
            user_name,
            created_date
        ))

# Funcția apelată când se apasă pe butonul Accept
def on_accept_button_click():
    selected_item = tree.selection()
    if not selected_item:
        messagebox.showwarning("Warning", "No post selected.")
        return

    post_id = tree.item(selected_item, "values")[0]
    accept_post(post_id)
    refresh_posts()

# Funcția apelată când se apasă pe butonul Remove
def on_remove_button_click():
    selected_item = tree.selection()
    if not selected_item:
        messagebox.showwarning("Warning", "No post selected.")
        return

    post_id = tree.item(selected_item, "values")[0]
    remove_post(post_id)
    refresh_posts()

def delete_post():
    selected_item = tree.selection()
    if not selected_item:
        messagebox.showwarning("Warning", "No post selected.")
        return

    post_id = tree.item(selected_item, "values")[0]
    if delete_post_from_server(post_id):
        refresh_posts()

# Funcția pentru pagina de login
def show_login_page():
    global root, current_user_name, current_user_email

    # Distruge fereastra curentă
    for widget in root.winfo_children():
        widget.destroy()

    # Configurarea interfeței de login
    root.title("Login")
    root.geometry("400x200")

    Label(root, text="Name:").pack(pady=5)
    name_entry = Entry(root)
    name_entry.pack(pady=5)

    Label(root, text="Email:").pack(pady=5)
    email_entry = Entry(root)
    email_entry.pack(pady=5)

    def on_login():
        name = name_entry.get().strip()
        email = email_entry.get().strip()
        if not name or not email:
            messagebox.showwarning("Warning", "Both fields are required!")
            return

        if login_user(name, email):
            current_user_name = name
            current_user_email = email
            open_main_page()
        else:
            messagebox.showerror("Error", "Invalid name or email.")

    Button(root, text="Login", command=on_login).pack(pady=10)

def open_comments_popup():
    selected_item = tree.selection()
    if not selected_item:
        messagebox.showwarning("Warning", "No post selected.")
        return

    post_id = tree.item(selected_item, "values")[0]
    post_title = tree.item(selected_item, "values")[1]
    # Creează fereastra popup
    popup = tk.Toplevel(root)
    popup.title(f"Comments for Post {post_id} - {post_title}")
    popup.geometry("600x400")

    # Crearea tabelului pentru comentarii
    columns = ("ID", "User", "Content")
    comments_tree = ttk.Treeview(popup, columns=columns, show="headings")

    for col in columns:
        comments_tree.heading(col, text=col)
        comments_tree.column(col, width=150)

    comments_tree.pack(fill="both", expand=True, padx=10, pady=10)

    # Funcție pentru a încărca comentariile în tabel
    def refresh_comments():
        for row in comments_tree.get_children():
            comments_tree.delete(row)

        comments = get_comments_for_post(post_id)

        for comment in comments:
            comments_tree.insert("", "end", values=(
                comment["id"],
                comment["userName"],
                comment["content"]
            ))

    # Funcție pentru a șterge un comentariu selectat
    def delete_comment():
        selected_comment = comments_tree.selection()
        if not selected_comment:
            messagebox.showwarning("Warning", "No comment selected.")
            return

        comment_id = comments_tree.item(selected_comment, "values")[0]
        if delete_comment_from_server(post_id, comment_id):
            refresh_comments()

    # Buton pentru Refresh
    refresh_button = ttk.Button(popup, text="Refresh", command=refresh_comments)
    refresh_button.pack(side="left", padx=10, pady=10)

    # Buton pentru Delete
    delete_button = ttk.Button(popup, text="Delete", command=delete_comment)
    delete_button.pack(side="left", padx=10, pady=10)

    # Încarcă comentariile la deschiderea popup-ului
    refresh_comments()

def get_comments_for_post(post_id):
    try:
        response = requests.get(f"{BASE_URL}/getCommentsByPost/{post_id}")
        if response.status_code == 200:
            return response.json()
        else:
            messagebox.showerror("Error", f"Failed to fetch comments for post {post_id}.")
            return []
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
        return []
    
def delete_comment_from_server(post_id, comment_id):
    url = f"{BASE_URL}/deleteComment/{post_id}/{comment_id}"
    try:
        response = requests.delete(url)
        if response.status_code == 204:
            messagebox.showinfo("Success", "Comment deleted successfully.")
        elif response.status_code == 404:
            messagebox.showerror("Error", "Comment not found or does not belong to the specified post.")
        else:
            messagebox.showerror("Error", f"Failed to delete comment. Status code: {response.status_code}")
    except requests.exceptions.RequestException as e:
        messagebox.showerror("Error", f"An error occurred: {e}")
# Configurarea interfeței grafice cu Tkinter
root = Tk()
show_login_page()
root.mainloop()