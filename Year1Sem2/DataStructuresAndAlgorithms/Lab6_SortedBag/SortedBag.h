#pragma once
//DO NOT INCLUDE SORTEDBAGITERATOR

#include <vector>

//DO NOT CHANGE THIS PART
typedef int TComp;
typedef TComp TElem;
typedef bool(*Relation)(TComp, TComp);
#define NULL_TCOMP -11111;

class SortedBagIterator;

//structure to represent a node for linked representation with dynamic allocation
struct BSTNode {
    TComp info;
    BSTNode* left;
    BSTNode* right;
    BSTNode* parent;
};

class SortedBag {
	friend class SortedBagIterator;

private:
	//TODO - Representation
    BSTNode* root;
    int length;
    Relation relation;

public:
	//constructor
	SortedBag(Relation r);
    
    BSTNode* initNode(TComp e, BSTNode* parent);

    BSTNode* insert_rec(BSTNode* node, TComp e);
    
	//adds an element to the sorted bag
	void add(TComp e);
    
    BSTNode* minimum(BSTNode* node);
    BSTNode* maximum(BSTNode* node);

	//removes one occurence of an element from a sorted bag
	//returns true if an eleent was removed, false otherwise (if e was not part of the sorted bag)
	bool remove(TComp e);

    bool search_rec(BSTNode* node, TComp elem) const;
	//checks if an element appearch is the sorted bag
	bool search(TComp e) const;

    int nrOccurences_rec(BSTNode* node, TComp e) const;
	//returns the number of occurrences for an element in the sorted bag
	int nrOccurrences(TComp e) const;

	//returns the number of elements from the sorted bag
	int size() const;

	//returns an iterator for this sorted bag
	SortedBagIterator iterator() const;

	//checks if the sorted bag is empty
	bool isEmpty() const;
    
    //counts and returns the number of unique elements in the SortedBag
    int uniqueCount() const;

	//destructor
	~SortedBag();
};
