## auth

Core plugin for the auth server, prompts players to input their token, register and login

After a successful login it pings BungeeCord (Waterfall) to send the player to the main server

**This plugin requires a mariadb server running on port 3306, the right database structure and an account**

The database can be set up with the 'chair' node server on the mailtoken-mc github.

Configure **plugins/auth/config.yml** with your database account credentials
```json
db:
  name: dbname
  user: dbuser
  pass: dbpass
```