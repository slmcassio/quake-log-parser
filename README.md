Setup
----------------------------------------------------------
On command line run: `java -jar path/to/quake-log-parser.jar`

**Java 8 is required**


Solution
----------------------------------------------------------
The solution was based on three major rules:

1. Use the `InitGame` tag to delimiter a game.

2. Split the `ClientUserinfoChanged` into **UserId** and **UserName** 
		
		2.1. e.g. `20:34 ClientUserinfoChanged: 2 n\Isgalamido\t\0\`, **UserId**: 2 and **UserName**: Isgalamido

3. Analyze the `Kill` line based on the information retrieved from Step2 and discover the **UserName** of the players based on the **UserId**
		
		3.1. e.g. `22:06 Kill: 2 3 7 ...`
				
				3.1.1. The number 2 represents the **UserId** of the killer player.
				
				3.1.2. The number 3 represents the **UserId** of the dead player.
				
				3.1.3. The number 7 represents an item on the  **MeansOfDeath** list.
				
The information extracted from the rules where used to generate some *Classes*:				
1. **Game:** holds all information from a single game
2. **Player:** represents a single player
3. **PlayerKillInfo:** holds the kill count of a single player
4. **PlayerDeathInfo:** holds the death count of a single player grouped by DeathType
5. **DeathType:** i.e. means of death

Some other useful information can be found on `javadoc`.