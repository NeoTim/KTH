Lab 1
-----

1. How many valid Host IP addresses belong to the subnet 172.16.128.0/18?

   2^14 - 2 = 16832

   Give the netmask for the subnet in dotted decimal format.

   255.255.192.0


2. What is ARP and why is it needed?

   ARP stands for address resolution protocol. It is used to map the IP address
   (used at the network layer) to a hardware address (used at the data link
   layer).

   In ethernet world, ARP gives you the 48-bit MAC address associated with
   the target network device, given it's IP address.


3. Explain the role of the port numbers in the transport layer.

   Having port numbers allows multiple applications to use a single IP address.

   You can stream music, and play WoW on your computer at the same time
   because of port numbers. A port number identifies an application.


4. Write the ttcp commands for both sender and receiver, which executes the
   following scenario: Send a TCP stream from host 10.2.3.4 to 10.4.5.6 on
   port 3333. The sender should send 4000 bytes with four datagrams of 1000
   bytes in each datagram.

   ./ttcp -t -n 4 -l 1000 -p 3333 -s 10.4.5.6 # sender side

   ./ttcp -r -p3333 -s # receiver side


5. Suppose a TCP sender receives an ACK packet in which the acknowledgement
   number is set to 12345 and the window size is 2048. Which sequence numbers
   can the sender transmit?

   12345 up to and including 14393


6. Briefly describe the following algorithms and when they are used:

   • Nagle’s algorithm.

   Nagle's algorithm is used to avoid small (and inefficient) TCP/IP
   transmissions, by essentially buffering up data.

   Since it buffers data, it affects latency, and interactivity adversely.

   I have only heard about Nagle's algorithm being disabled (gaming, music
   streaming), so I can't say where it is really really required, and used in
   practice.

   Maybe it makes sense for poorly written TCP applications, which try to
   transmit many small messages.

   • Karn’s algorithm.

   See the answer to Question 7.

   • Delayed acknowledgement.

   Delayed acknowledgement is a hack to improve the network performance. It
   allows the receiver to combine several ACK responses into a single ACK (which
   reduces network traffic).

   A receiver may delay sending an ACK response by up to 500 ms.

   Combining delayed acknowledgement with Nagle's algorithm can be problematic
   (in terms of performance, and latency) for some applications. It seems that
   combining Nagle's algorithm with almost anything is a problem ;)

   • Piggybacked acknowledgement.

   Piggybacked acknowledgement allows the receiver to combine the ACK message
   with the data frame (as opposed to sending the ACK control frame separately).

   This reduces the number of segments that the sender has to send, and hence
   make the network utilization more efficient.


7. How is the retransmission timeout (RTO) value computed in TCP?

   RTO stands for Retransmission Timeout, and it determines the time the TCP
   sender waits for the ACK (acknowledgement) of the sent packet.

   RTT = (α * Old_RTT) + ((1 − α) * New_RTT)

   α is the weighing factor, and determines how quickly the new RTT will
   respond to the changes in network delay.

   Retransmitted segments make RTT estimates ambiguous, and to solve this,
   Karn's algorithm ignores retransmitted segments when calculating the
   new RTT value. Also, RTO should be doubled after retransmissions to avoid
   TCP from using the old RTT value, and continuing to retransmit segments.


8. Explain the following TCP mechanisms:

   • Sliding window flow control.

   A Sliding window allows unlimited number of packets to be communicated while
   using fixed-size sequence numbers (32-bit in TCP).

   The word "window" refers to the maximum amount of data the sender can send
   without waiting for an ACK from the sender side.

   After getting the ACK, the sender repositions the "window", and transmission
   continues from the packet next to the last transmitted one.


   Sender                 Receiver
   ------                 --------


   [0 1] 2 3 4 5 6  --->
                           1 0

                     ACK0
                    <----


   0 [1 2] 3 4 5    --->
     ^
     |                     2
     |
     |
     ----- the "window" slides over after the ACK is seen :-)


   [ ] represents the windows, CWND size = 2

   CWND => Current Window

   Reference: http://tnlandforms.us/tcptour/javis/tcp_slidwin.html (great page)


   • Slow start and congestion avoidance.

   Slow start is a congestion control strategy used by TCP by adapt to
   different, and changing network conditions.

   When using slow start, TCP starts with a small congestion window size
   (CWND).  This congestion window size is increased (almost doubled every RTT)
   with each ACK received. This increase in the transmission rate continues
   unless a packet loss is detected *or* the receiver windows size becomes a
   bounding factor among other things *or* the slow start threshold value is
   reached.

   When CWND reaches the slow start threshold value, the windows size is
   increased by 1 segment for every RTT (instead of being almost doubled).  TCP
   at this point has moved from using slow start to doing congestion avoidance.


   • Fast retransmit and fast recovery.

   Fast Retransmit is a TCP tweak to improve performance by reducing the time a
   sender has to wait before retransmitting a lost segment. Fast retransmit is
   based on the idea of duplicate acknowledgement. When the receiver receives
   an out-of-order segment, it tells the sender that it is expecting an earlier
   segment (with a specific sequence number). If the sender sees multiple such
   duplicate (three in practice) "stuck" ACK messages, then it can be
   reasonably sure that the segment in question has been lost, and it then
   retransmits the segment being requested.

   When the fast retransmit mechanism detect a lost segment, the sender can
   choose to *not* use the slow start mechanism (to increase the transmission
   rate again from the bottom). Since the sender has *already* received
   multiple duplicate ACKs, corresponding to the correct delivery of several
   out-of-order segments), the sender can be a bit optimistic about the state
   of congestion in the network.


References
----------


* The TCP/IP Guide (http://www.tcpipguide.com/index.htm)

* Wikipedia :-)
