

# Add users
users = [
    {"name": "Alice", "email": "alice@example.com"},
    {"name": "Bob", "email": "bob@example.com"},
    {"name": "Charlie", "email": "charlie@example.com"}
]

for user in users:
    neo4j_driver.add_user(user["name"], user["email"])

neo4j_driver.close()