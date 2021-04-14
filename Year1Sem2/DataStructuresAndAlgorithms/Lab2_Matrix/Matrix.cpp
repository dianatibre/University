#include "Matrix.h"
#include <exception>
using namespace std;

int Matrix::relation(Node* node1, Node* node2){
    //complexity: theta(1)
    // returns -1 if node 1 should be placed in front of node 2;
    // returns 0 if the position of node 1 is equal to the position of node 2;
    // returns 1 if node 1 should be placed after node 2;
    if (node1->line<node2->line)
        return -1;
    if (node1->line==node2->line) {
        if (node1->column<node2->column)
            return -1;
        if (node1->column==node2->column)
            return 0;
    }
    return 1;
}

Matrix::Matrix(int nrLines, int nrCols) {
    //complexity: theta(1)
	//TODO - Implementation
    this->nLines=nrLines;
    this->nColumns=nrCols;
    this->head=NULL;
    this->tail=NULL;
}


int Matrix::nrLines() const {
	//TODO - Implementation
	//complexity: theta(1)
    return this->nLines;
}


int Matrix::nrColumns() const {
	//TODO - Implementation
	//complexity: theta(1)
    return this->nColumns;
}


TElem Matrix::element(int i, int j) const {
	//TODO - Implementation
    //complexity:
    //best case: theta(1)
    //worst case: theta(n)
    //average case: theta(n)
    //total complexity: O(n)
    
    //throws exception if (i,j) is not a valid position in the Matrix
    if (i<0 || j<0 || i>=this->nLines || j>=this->nColumns)
        throw exception();
    
    //if the matrix is empty, it returns the NULL_TELEM
    if (this->head==NULL)
        return NULL_TELEM;
    
    //if the element is the head of the matrix, it will be returned
    if (this->head->line==i && this->head->column==j)
        return this->head->value;
    
    //if the element is the tail of the matrix, it will be returned
    if (this->tail->line==i && this->tail->column==j)
        return this->tail->value;
    
    //else, the function will search for the element in the matrix
    if (this->head!=this->tail) {
        Node* current = this->head->next;
        while (current->next!=NULL) {
            if (current->line==i && current->column==j)
                return current->value;
            current=current->next;
        }
    }
    
	return NULL_TELEM;
}

TElem Matrix::modify(int i, int j, TElem e) {
    //complexity
    //best case: theta(1)
    //worst case: theta(n^2)
    //average case: theta(n^2)
    //total complexity: O(n^2)
    
	//TODO - Implementation
    
    //throws exception if (i,j) is not a valid position in the Matrix
    if (i<0 || j<0 || i>=this->nLines || j>=this->nColumns)
        throw exception();
    
    //create a new node with the parameters
    Node* newNode = new Node;
    newNode->line=i;
    newNode->column=j;
    newNode->value=e;
    newNode->next=NULL;
    
    //adds the node if the matrix is empty
    if (this->head==NULL)
    {
        this->head=newNode;
        this->tail=newNode;
        return NULL_TELEM;
    }

    //else
    if (e==NULL_TELEM) {
        // if e=0, we will delete the node
        if (relation(newNode, this->head)==0) {
            if (relation(newNode, this->tail)==0) {
                TElem prev=this->head->value;
                this->tail=NULL;
                this->head=NULL;
                return prev;
            }
            else {
                TElem prev = this->head->value;
                this->head=this->head->next;
                return prev;
            }
        }
        else if (relation(newNode, this->tail)==0) {
            TElem prev = this->tail->value;
            Node* previous = this->head;
            while (previous->next!=this->tail)
                previous=previous->next;
            this->tail=previous;
            return prev;
        }
        else {
            Node* current=this->head;//->next;
            while (current->next!=NULL) {
                if (relation(newNode, current)==0) {
                    TElem prev = current->value;
                    Node* previous=this->head;
                    while (previous->next!=current)
                        previous=previous->next;
                    previous->next=current->next;
                    return prev;
                }
                current=current->next;
            }
        }
    }
    else {
        //if e != 0, we will modify the node
        if (relation(newNode, this->head)==-1) {
            newNode->next=this->head;
            this->head=newNode;
            return NULL_TELEM;
        }
        else if (relation(newNode, this->tail)==1) {
            this->tail->next=newNode;
            this->tail=newNode;
            return NULL_TELEM;
        }
        else if (relation(newNode, this->head)==0) {
            TElem prev=this->head->value;
            this->head->value=e;
            return prev;
        }
        else if (relation(newNode, this->tail)==0) {
            TElem prev = this->tail->value;
            this->tail->value=e;
            return prev;
        }
        else {
            Node* current = this->head;
            while (current!=NULL) {
                if (relation(newNode, current)==0) {
                    TElem prev=current->value;
                    current->value=newNode->value;
                    return prev;
                }
                if (relation(newNode, current)==-1){
                    Node* previous = this->head;
                    while (previous->next!=current) {
                        previous=previous->next;
                    }
                    previous->next=newNode;
                    newNode->value=e;
                    newNode->next=current;
                    return NULL_TELEM;
                }
                current=current->next;
                }
            
            }
        
        }
    return NULL_TELEM;
}
    

void Matrix::setMainDiagonal(TElem elem) {
    
    //complexity
    //best case: theta(1)
    //worst case: theta(n^3)
    //average case: theta(n^3)
    //total complexity: O(n^3)
    
    if (this->nColumns!=this->nLines)
        throw exception();
    
    
    for (int i=0; i<this->nLines; i++) {
        Node* newNode = new Node;
        newNode->line=i;
        newNode->column=i;
        newNode->value=elem;
        newNode->next=NULL;
        
        //adds the node if the matrix is empty
        if (this->head==NULL)
        {
            this->head=newNode;
            this->tail=newNode;
            continue;
        }
        
        //if the matrix has only one element and its position is not the same as the newNode's, it adds the newNode as the tail
        if (this->head==this->tail && relation(newNode, this->head)!=0) {
            this->head->next=newNode;
            this->tail=newNode;
            continue;
        }
        //if the newNode has the same position as the head of the matrix, only the value will be modified
        else if (relation(newNode, this->head)==0) {
            this->head->value=elem;
            continue;
        }
        
        //if the newNode should be placed after the tail of the matrix (because of the relationship), the tail will be modified too
        if (relation(newNode, this->tail)==1) {
            this->tail->next=newNode;
            this->tail=newNode;
            continue;
        }
        
        //if the newNode has the same position as the tail of the matrix, only the value will be modified
        if (relation(newNode, this->tail)==0) {
            this->tail->value=elem;
            continue;
        }
        
        //if the newNode does not match with the head or the tail or before the head or before the tail, the matrix will be parsed to find its right position
        Node* current = this->head;
        while (relation(newNode, current)!=1 && current->next!=NULL) {
            if (relation(newNode, current)==0) {
                current->value=elem;
                continue;
            }
            current=current->next;
        }
        Node* previous = this->head;
        while (relation(previous->next, current)==-1 && previous->next!=NULL) {
            previous=previous->next;
        }
        previous->next=newNode;
        newNode->value=elem;
        newNode->next=current;
        continue;
        
    }
    
}
