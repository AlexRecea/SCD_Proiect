import requests
from tkinter import Tk, ttk, messagebox, Label, Entry, Button

# URL-ul de bază al serverului
BASE_URL = "http://localhost:8083"

# Variabile globale pentru utilizatorul logat
current_user_name = None
current_user_email = None

# Funcția pentru a verifica dacă utilizatorul există
def login_user(name, email):
    try:
        response = requests.get(f"{BASE_URL}/getAllUsers")
        if response.status_code == 200:
            users = response.json()
            for user in users:
                if user["name"] == name and user["email"] == email:
                    return True
            return False
        else:
            messagebox.showerror("Error", "Failed to fetch users.")
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
    root.title(f"Post Management - Logged in as {current_user_name}")
    root.geometry("900x500")

    # Crearea tabelului (Treeview)
    columns = ("ID", "Title", "Content", "Status", "User ID", "Date")
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

    # Încărcarea postărilor la pornire
    refresh_posts()

# Funcția pentru a încărca toate postările
def get_all_posts():
    try:
        response = requests.get(f"{BASE_URL}/getAllPosts")
        if response.status_code == 200:
            return response.json()
        else:
            messagebox.showerror("Error", "Failed to fetch posts.")
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
        tree.insert("", "end", values=(post["id"], post["title"], post["content"], post["status"], post["user_id"], post["created_on"]))
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

# Configurarea interfeței grafice cu Tkinter
root = Tk()
show_login_page()
root.mainloop()