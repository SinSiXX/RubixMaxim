RubixMaxim
===================
***This is not intended to be instantiated, and I would prefer you not to. Instead, invite Rubix to your server at https://discordapp.com/oauth2/authorize?client_id=168321743302295552&scope=bot***

RubixMaxim is bot a rewrite, port, and merge of 2 previous projects, Rubix and Maxim.
Both were initially created in Python and are being ported into Java.

----------
> **Prerequisites:**

> - [JDA](https://github.com/DV8FromTheWorld/JDA) 1.4.0_238
> - [GSON](https://github.com/google/gson)
> - [JDBC](https://dev.mysql.com/downloads/connector/j/5.0.html)

----------
> **Commands:**

> - !help [ command ]
> - !about
> - !aliases [ command ]
> - !roll < NdN >
> - !whatgame
> - !say < phrase >
> - !osu < username >
> - !redact
> - !op < username or mention >
> - !afk
> - !id < username or multiple mentions >
> - !leave
> - !config [ setting ] [ value ]
> - !banword < phrase >
> - !listops
> - !whois [ username or mention ]
> - !vote < username > <+ or ->
> - !colorme < rgb value >

----------

> **Settings**

> - **DoModerate** - (*Default:* **1**) Turns on/off the automoderator feature.
> - **AllowLewd** - (*Default:* **0**) **CURRENTLY HAS NO EFFECT.**
> - **MaxWarns** - (*Default:* **3**) **CURRENTLY HAS NO EFFECT.**
> - **DoGreet** - (*Default:* **0**) Turns on/off Rubix's greeting feature.
> - **Greeting** - (*Default:* **Welcome, {USER}!**) Changes Rubix's greeting message. {USER} will be replaced with their name.
> - **Farewell** (*Default:* **Goodbye, {USER}!**) Same as Greeting, except is said when a user leaves.
> - **Prefix** (*Default:* **!**) Sets the prefix Rubix will respond to. You can use **@Rubix prefix** to get his current prefix, regardless of what it may be.
> - **CommandNotFoundMsg** (*Default:* **0**) Turns on/off Rubix's command not found message.
> - **AllowColors** (*Default:* **0**) Allows/disallows use of colorme.
