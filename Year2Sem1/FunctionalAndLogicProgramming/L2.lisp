; 13. For a given tree of type (2) return the path from the root node to a certain given node X.

;(node (list-subtree-1) (list-subtree-2)


;mathematical model
; path(l list, e node) = { false, if e is not a node in the tree
;                         root, if e is the root of the tree
;                         root + path( left_subtree, e), if e is in the left subtree
;                         root + path( right_subtree, e), else


; appears(list l, node e) = { false, if l is null
;                             true, if e is the root
;                             true, if appears(left subtree, e) or appears(right_subtree, e)

(defun appears (l e) ; verifies if e appears in the list l ( if e is a node of the tree)
    (cond 
        ((null l) nil) ; if l=null => nil
        ((equal (car l) e) t ) ; if l1=e => t
        (t (or (appears (cadr l) e) (appears (caddr l) e))) ; else => appears(l2 e) or appears (l3 e)
    )
)

;(print (appears '(A (B) (C (D) (E))) 'A))
;=> T

;(print (appears '(A (B) (C (D) (E))) 'E))
;=> T

;(print (appears '(A (B) (C (D) (E))) 'F))
;=> NIL

(defun path (l e) ; l = list of nodes in the specific form, e = given node
    (cond
        ((not (appears l e)) nil) ; if e is not a node => nil
        ((equal (car l) e)(list  e) ) ; if e is the root => list(root)
        ((appears (cadr l) e) (cons (car l) (path (cadr l) e))) ; if e is a node in the left subtree => root + path (l2 e)
        (t (cons (car l) (path (caddr l) e))) ; else => root + path(l3 e)
    )
)    

;1
(print (path '(A (B) (C (D) (E))) 'D)) ; => (A C D) 
(print (path '(A (B) (C (D) (E))) 'A)) ; => (A)
(print (path '(A (B) (C (D) (E))) 'B)) ; => (A B)
(print (path '(A (B) (C (D) (E))) 'E)) ; => (A C E)

(print (path '(A (B) (C (D) (E (X)))) 'X )) ;


;2
(print (path '(A (B (C) (D))(E (F (G) (H)))) 'H)) ; => (A E F H)

(print (path '(A (B (C (Y) (Z)) (D)) (E (F (G (X))) (H))) 'Z))



;1
;      A
;     / \
;    B   C
;        /\
;       D  E
;         /
;        X


;2
;          A
;        /   \
;       B     E
;      / \     \
;     C   D     F
;    / \       / \
;  Y   Z      G   H
;            /
;           X