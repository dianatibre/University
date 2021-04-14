#pragma once

//DO NOT CHANGE THIS PART
typedef int TElem;
#define NULL_TELEM 0

struct Node{
    int line, column;
    TElem value;
    Node *next;
};

class Matrix {

private:
	//TODO - Representation
    int nLines, nColumns;
    Node *head, *tail;
    
    // returns -1 if node 1 should be placed in front of node 2;
    // returns 0 if the position of node 1 is equal to the position of node 2;
    // returns 1 if node 1 should be placed after node 2;
    int relation(Node* node1, Node* node2);
    
public:
	//constructor
	Matrix(int nrLines, int nrCols);

	//returns the number of lines
	int nrLines() const;

	//returns the number of columns
	int nrColumns() const;

	//returns the element from line i and column j (indexing starts from 0)
	//throws exception if (i,j) is not a valid position in the Matrix
	TElem element(int i, int j) const;

	//modifies the value from line i and column j
	//returns the previous value from the position
	//throws exception if (i,j) is not a valid position in the Matrix
	TElem modify(int i, int j, TElem e);
    
    //sets all the values from the main diagonal to the value elem.
    //throws an exception if it is not a square matrix.
    void setMainDiagonal(TElem elem);

};
