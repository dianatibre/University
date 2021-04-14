; 9.
; a) Write a function that merges two sorted linear lists and keeps double values.

;mathematical model
; merge_sorted_l(list a, list b) = { b, if a is null
;    a, if b is null
;    a1 + merge_sorted_l(a2a3..an,b), if a1<=b1
;    b1 + merge_sorted_l(a, b1b2..bn), else


(defun merge_sorted_l (a b) ; a, b lists
	(cond
		((null a) b) ; if a is null => b
		((null b) a) ; if b is null => a
		((<= (car a) (car b)) (cons (car a) (merge_sorted_l (cdr a) b))) ; if a1 <= b1 => a1 + merge_sorted_l(a2a3..an,b)
		(T (cons (car b) (merge_sorted_l a (cdr b)))) ; else => b1 + merge_sorted_l(a, b1b2..bn)
	)
)

(write-line "a)")

(print (merge_sorted_l '(1 2 3 5) '(4 6)))
; => (1 2 3 4 5 6)

(print (merge_sorted_l '(1 2 2 3 3) '(3 4 4 5 5 7)))
; => (1 2 2 3 3 3 4 4 5 5 7)

(print (merge_sorted_l '() '(1 3 5 7 9)))
; => (1 3 5 7 9)

; b) Write a function to replace an element E by all elements of a list L1 at all levels of a given list L.

;mathematical model
;myreplace(list l, elem e, list k) = { k, if l=atom and l=e
;        l, if l is atom   
;        myreplace(l1,e,k) U ... U myreplace(ln,e,k), else

(defun myreplace (l e k) ; l = L, e = elem, k=L1
    (cond
        ((and (atom l) (equal e l)) k) ; if l is atom and l=e => k
        ((atom l) l) ; if l is atom => l
        (T (mapcar #'(lambda (l) (myreplace l e k)) l)) ; else calls replace for each element from the list l
    )
)

(write-line " ")
(write-line " ")
(write-line "b)")

(print (myreplace '(1 2 10 10 3 (10 1)) 10 '(0)))
;=> (1 2 (0) (0) 3 ((0) 1))

(print (myreplace '(1 0 2 3 (0 0) 0) 0 '(99 100)))
;=> (1 (99 100) 2 3 ((99 100) (99 100)) (99 100))

(print (myreplace '(1 2 3 (3 4 5) ((3) 3)) 3 '(0 0)))
;=> (1 2 (0 0) ((0 0) 4 5) (((0 0)) (0 0)))

; c) Write a function to determines the sum of two numbers in list representation, and returns the
; corresponding decimal number, without transforming the representation of the number from list to
; number.


; decimal(number l, number k, number c) = { (k+c) mod 10, if l=null
;                                           (l+c) mod 10, if k=null
;                                           (l+k+c) mod 10, else

(defun decimal (l k c) ; returns the decimal number
    (cond
        ((null l) (mod (+ k c) 10)) ; if l = null => (k+c) mod 10
        ((null k) (mod (+ l c) 10)) ; if k = null => (l+c) mod 10
        (T (mod (+ l k c) 10)) ; else => (l+k+c) mod 10
    )
)

(write-line " ")
(write-line " ")
(write-line "c)")

;(print (decimal 1 2 3))
;=>6

;(print (decimal 10 nil 12))
;=>2

;(print (decimal nil 9 1))
;=> 0

; carry(number l, number k, number c) = { ((k+c)-((k+c) mod 10))/10, if l=null and (k+c)-((k+c) mod 10) > 9
;                                         (k+c) mod 10, if if l=null and (k+c)-((k+c) mod 10) > 9
;                                         ((l+c)-((l+c) mod 10))/10, if k=null and (l+c)-((l+c) mod 10) > 9
;                                         (k+c) mod 10, if if l=null and (k+c)-((k+c) mod 10) > 9
;                                         ((l+k+c)-((l+k+c) mod 10))/10, if ((l+k+c)-((l+k+c) mod 10) >9 )
;                                         (l+k+c) mod 10, else


(defun carry (l k c) ; computes the one digit carry for the sum operation 
    (cond
        ((null l) (if (> (- (+ k c) (mod (+ k c) 10)) 9) 
                      (/ (- (+ k c) (mod (+ k c) 10)) 10) 
                      (mod (+ k c) 10)
                  )
        ) ; if l=null => if (k+c)-((k+c) mod 10) > 9 then ((k+c)-((k+c) mod 10))/10  else (k+c) mod 10
        ((null k) (if (> (- (+ l c) (mod (+ l c) 10)) 9)
                      (/ (- (+ l c) (mod (+ l c) 10)) 10) 
                      (mod (+ l c) 10)
                  )
        ) ; if k=null => if (l+c)-((l+c) mod 10) > 9 then ((l+c)-((l+c) mod 10))/10  else (l+c) mod 10
        (T (if (> (- (+ l k c) (mod (+ l k c) 10)) 9)
                      (/ (- (+ l k c) (mod (+ l k c) 10)) 10) 
                      (mod (+ l k c) 10)
                  )
        ) ; else => if ((l+k+c)-((l+k+c) mod 10) >9 ) then ((l+k+c)-((l+k+c) mod 10))/10  else (l+k+c) mod 10
    )
)

;(print (carry 9 nil 1))
;=> 1

;(print (carry 7 8 9))
;=>2

;(print (carry 1 8 9))
;=>1

(defun my_append (l k) ; appends an element (represented as a list) to a list
    (if (null l) 
        k
        (cons (car l) (my_append (cdr l) k))
    ) ; if l=null then k else (l1 + my_append(l2l3...ln k))
)

;(print (my_append '(1 2 3) '(4)))
;=> (1 2 3 4)

(defun my_reverse (l) ; returns the list l reversed
    (cond
        ((null l) nil) ; if l=null=>nil
        ((listp (car l)) (my_append (my_reverse (cdr l)) (list (my_reverse (car l))))) ; if l1 is a list => my_append(my_reverse(l2..ln) list(my_reverse(l1)))
        (T (my_append (my_reverse (cdr l)) (list (car l)))) ; else => my_append(my_reverse(l2..lb) list(l1))
    )
)

;(print (my_reverse '(1 2 3 4)))
;=>(4 3 2 1)

(defun sumList (l k c) ; computes the sum (digit by digit) of lists l and k 
    (cond
        ((and (null l) (null k)) (if (= 1 c) (list 1) nil)) ; if l=null and k=null => (if c=1 then list(1) else nil)
        (T (my_append (sumList (cdr l) (cdr k) (carry (car l) (car k) c)) (list (decimal (car l) (car k) c)))) ; else => my_append(sumList(l2...ln k2...kn carry(l1 k1 c)) list(decimal(l1 k1 c))))    
    )
)

; solve(list l, list k) = { sumList(reverse(l),reverse(k),0) }

; sumlist(list k, list k, number c) = { c, if l=null and k=null
;                                    my_append(sumList(l2...ln, k2...kn, carry(l1, k1, c)) list(decimal(l1, k1, c)))), else

; myreverse(list l) = { false, if l=null
;                       my_append(my_reverse(l2..ln) list(my_reverse(l1))), if l1 is a list
;                       my_append(my_reverse(l2..lb) list(l1)), else

; my_append(list l, list k) = { k, if l is null
;                               l1 U my_append(l2l3...ln, k)


(defun solve (l k) ; main function, computes the sum between lists l and k and starts with carry 0; the lists should be reversed because we parse them from beginning to end
    (sumList (my_reverse l) (my_reverse k) 0) ; sumList(l reversed, k reversed, carry=0)
)

(print (solve '(9 9) '(1 2)))
;=> (1 1 1)

(print (solve '(9 9) '(9 9)))
;=> (1 9 8)

(print (solve '(9 9 9) '(9 9 1)))
;=> (1 9 9 0)

; d) Write a function to return the greatest common divisor of all numbers in a linear list.

;mathematical model
;gcd(a,b) = { a, if b=0
;         gcd(b, a mod b), else }

(defun mygcd (a b) ; computes the gcd between two elements
    (cond
        ((and (not (numberp a)) (not (numberp b))) nil) ; if a not number and b not number => nil
        ((not (numberp a)) b) ; if a not number => b
        ((not (numberp b)) a) ; if b not number => a
        ((equal b 0) a) ; if b=0 => a
        (T (mygcd b (mod a b))) ; else mygcd(b (a mod b))
    )
)

;list_gcd(list l) = { l1, if l1=atom and l1 is the only element in l
;                     mygcd(list_gcd(l1) list_gcd(l2..ln)), if l1=list
;                     mygcd(l1 list_gcd(l2..ln)), else

(defun list_gcd (l) ; computes the gcd on the entire list
    (cond
        ((and (atom (car l)) (null (cdr l))) (car l)) ; if l1=atom and l2..ln=nil => l1 
        ((listp (car l)) (mygcd (list_gcd (car l)) (list_gcd (cdr l)))) ; if l1=list => mygcd(list_gcd(l1) list_gcd(l2..ln))
        (T (mygcd (car l) (list_gcd (cdr l)))) ; else => mygcd(l1 list_gcd(l2..ln))
    )
)

(write-line " ")
(write-line " ")
(write-line "d)")

(print (list_gcd '(24 ( 16 (12 A B)) 72)))
;=> 4

(print (list_gcd '(10 (100 20 a b 40) 1000 2000 c)))
;=> 10



(defun f(f k)
  (cond
   ((list(car(l)) (f(cdr(l) k))))
    (T (atom(car(l)) (list((/ l k) f(cdr(l) k)))))
  )
)


(print (f( '(2 4 (2 4)) '2)))