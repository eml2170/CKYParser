# CKYParser
Parses PCFG using CKY algorithm

Run the shell script run.sh. It should take several seconds.

The performance prints to stdout as precision/recall/F1 for each part of speech:
      Type       Total   Precision      Recall     F1 Score
===============================================================
         .         370     1.000        1.000        1.000
       ADJ         164     0.689        0.622        0.654
      ADJP          29     0.324        0.414        0.364
  ADJP+ADJ          22     0.591        0.591        0.591
       ADP         204     0.960        0.951        0.956
       ADV          64     0.759        0.641        0.695
      ADVP          30     0.417        0.167        0.238
  ADVP+ADV          53     0.700        0.660        0.680
      CONJ          53     1.000        1.000        1.000
       DET         167     0.988        0.994        0.991
      NOUN         671     0.795        0.845        0.819
        NP         884     0.617        0.548        0.580
    NP+ADJ           2     0.333        0.500        0.400
    NP+DET          21     0.944        0.810        0.872
   NP+NOUN         131     0.610        0.656        0.632
    NP+NUM          13     0.375        0.231        0.286
   NP+PRON          50     0.980        0.980        0.980
     NP+QP          11     0.750        0.273        0.400
       NUM          93     0.914        0.688        0.785
        PP         208     0.623        0.635        0.629
      PRON          14     1.000        0.929        0.963
       PRT          45     1.000        0.933        0.966
   PRT+PRT           2     0.286        1.000        0.444
        QP          26     0.650        0.500        0.565
         S         587     0.704        0.814        0.755
      SBAR          25     0.667        0.400        0.500
      VERB         283     0.790        0.813        0.801
        VP         399     0.663        0.677        0.670
   VP+VERB          15     0.294        0.333        0.312

     total        4664     0.742        0.742        0.742

The final score 74.2 is pretty good :)