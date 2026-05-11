How to play 'Battle to Wordle!':
begin by compiling all the files Client.java, Clienthandler.java, GameGUI.java, Main.java, and Server.java in the terminal. 
should all compile when user does javac Server.java Main.java in the correct directory.
then split the terminal based on how many clients/players you'd like playing.
I implore you to be as quick as possible with the timer being only 30 seconds which starts once the server begins.
do "java Server" to start the server and "java Main"
Server terminal will show the correct word everytime the round restarts and recieve guesses and send out messages.
Clients/Players will be shown the Graphical User Interface with a text box at the bottom that only uses 5 letter characters.
If the player guesses a word a broadcast will be sent to every Player notifying that another player has put in a guess.
If the guess is correct, a text saying You Win! will appear but the game will go on for other players to try and guess the word
too.
if the guess is incorrect, a text saying Wrong Guess: and for each letter, either an x or an o will be in place of that guesses
letters which indicates whether that letter is in the target word or not.