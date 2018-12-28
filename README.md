# STR_line
Station 1 receives parts according to the sequence “A”, “A”, “B”, “B”, ”C”, ”C” and so on, letting go any part which is out of the mentioned sequence. After a complete sequence, the lamp at P2.7 must flash (On/off) for 1 second.  

• Station 2 also receives parts according to the sequence “A”, “B”, “C”, “A”, ”B”, ”C” and so on, letting go any part which is out of the mentioned sequence. After a complete sequence, the lamp at P2.7 must flash for 2 seconds. 

• Station 3, or recycle station, receives all the parts that were rejected by station 1 and station 2. Whenever this happens, the mentioned lamp must frantically flash many times for one second. 

• If a human operator at a station presses the corresponding button P1.4 (station 1) or P1.3 (station 2) one, two or three times, then the next incoming part that must get into the station must be of type A, B and C respectively. Any other type goes to the recycle station. After this, the sequences in the corresponding station starts its operation with the defined sequence, {A,A,B,B,C,C} for station 1 and {A,B,C,A,B,C} for station 2.

• The system must remember the total number of parts of each type that got into each station, as well as the number of sequences. These numbers must be shown in the screen whenever requested.

To access the simulator you must use the link http://localhost:8081/LineSim2.html and have the respective html file saved 
