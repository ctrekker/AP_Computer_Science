These are the server files for this program. Unless you happen to have NodeJS installed, they won't run on
your computer. I have also not included an sort of instructions for running it, because in order to test the
installation, you would have to also modify 2 lines of the java source code, then recompile, then repackage,
and its just a complicated mess. In short, these files are here just in case you need the source code, for them
but don't actually do anything for the program on your own computer.

Since they're not in Java, I highly doubt it would be of much interest checking them out.

PROGRAM INFORMATION:
pokerserver.js - The main and single 1109 line program completely devoted to handling poker games. This script is
	located on burnscoding.com (my new, work-in-progress server). Debug info for the poker server can be found by
	visiting http://burnscoding.com:52.
downloadList.php - A script written in yet another language (PHP) which is devoted to telling the client about
	static files (like images) that are needed upon startup. This is placed on ctrekker.mjtrekkers.com (my old server).
checkRunning - A script written in yet ANOTHER language (this time its BASH), which is devoted to checking whether or
	not the server is currently running. Please not that it can be only be run on properly set up LINUX platforms
	(on 5 minute cron schedule on burnscoding.com)
package.json - A static file written in JSON (technically another language) which is used only for script compilation and
	dependency management.
README.txt - This file, which has instructions/details on the /server directory in the TexasHoldem release folder. This is
	not on any server, as it is unused by neither me or the server (I wrote it)
