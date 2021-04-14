;L3.8 Write a function to determine the number of nodes on the level k from a n-tree represented as follows:
; (root list_nodes_subtree1 ... list_nodes_subtreen)
;Eg: tree is (a (b (c)) (d) (e (f))) and k=1 => 3 nodes

;mathematical model
; GetNodesAtLevel(list Tree, integer K) = {
;       1, if K is the first level and Tree is a list
;       0, if the Tree is not a tree
;       GetNodesAtLevel(subtree1, K-1) + ... + GetNodesAtLevel(subtreeN, K-1) ,else



(defun GetNodesAtLevel (Tree K)
 ; (print Tree)
 ; (write K)
  (cond
   ; if Tree is a list and K equal 0 (K is first level) => 1
    ((and (listp Tree) (equal K 0))       
      1
    )
    ; if Tree is atom => 0
    ((atom Tree)
      0
    )
    ; if l2l3...ln = null => 0
    ((null (cdr Tree))  
      0
    )
    ; else GetNodesAtLevel(subtree1, K-1) + ... + GetNodesAtLevel(subtreeN, K-1)
    ; it applies the function for each sublist of Tree 
    (T
      (apply '+ (mapcar #'(lambda(A) (GetNodesAtLevel A (- K 1))) Tree)) 
    )

  )
)

(print (GetNodesAtLevel '(7 (2 (3)) (4) (5 (6))) 1) )
;(a (b (c)) (d) (e (f)))
;      a            7
;   b  d  e      2  4  5
;   c     f      3     6



(print (GetNodesAtLevel '(7 (1 (4 (10)) (5)) (2 (6 (11)) (7)) (3 (8) (9))) 2) ) 
;               7
;      1        2        3
;    4   5    6   7    8   9
;  10        11