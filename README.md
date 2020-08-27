# Moral Machine, an adaptation of the Trolley Dilemma

**Final Project for COMP90041 Semester 1, 2020.
<br />Lecturer: Dr. Tilman Dingler**
<br />
<br />
The idea of a Moral Machine stems from the *Trolley Dilemma*, a fictional scenario where a decision maker must decide between the lesser of two evils (Thomson, J. J. (1986) and Foot, P. (1967)). The adapted scenario involves an autonomous car that is approaching a pedestrian crossing. Its brakes suddenly fail and at that point, it is too late for the car's passengers to regain control of the car. As a result, the car must make a decision based on the situation:

 * Whether the pedestrian crosses legally (at the green light)
 * Characters (human and/or animal) involved in the situation as both pedestrians and passengers
 * Characteristics of these characters (available characteristics include *- but not limited to -* gender, bodytype, age, profession)

The aim of this project is to build a program that can simulate this decision-making experience. It can be played interactively using either pre-configured or randomly-generated scenarios that will be displayed to the console. Otherwise, the program has an algorithm that lets the computer decide on whom to be saved.
<br />
<br />
The program has an Audit feature that allows users to see the statistics of projected survival based on past decisions. The Audit details the decision maker (User or Algorithm), survival ratio of each characteristics, and the average age of survivors.
<br />
<br />
# Running the Moral Machine
Either of the following program calls will invoke the help:
<br /> __________________________________
<br /> $java EthicalEngine --help
<br /> $java EthicalEngine -h
<br /> __________________________________
<br />
<br /> The following help window will be displayed:
<br /> **=============================================**
<br /> EthicalEngine - COMP90041 - Final Project
<br /> Usage: java EthicalEngine [arguments]
<br /> Arguments:
<br /> -c or --config &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Optional: path to config file
<br /> -h or --help &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; Print Help (this message) and exit
<br /> -r or --results &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;Optional: path to results log file
<br /> -i or --interactive &nbsp; &nbsp; &nbsp;Optional: launches interactive mode
<br /> **=============================================**
<br />
<br />An example of command call, this will launch the program in the interactive mode with randomly-generated scenarios:
<br /> __________________________________
<br /> $java EthicalEngine -i
<br /> __________________________________
<br />
#
***Completed in June 2020***
