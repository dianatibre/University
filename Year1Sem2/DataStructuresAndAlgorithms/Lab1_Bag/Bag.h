#pragma once
//DO NOT INCLUDE BAGITERATOR


//DO NOT CHANGE THIS PART
#define NULL_TELEM -11111;
typedef int TElem;
class BagIterator; 
class Bag {

private:
	//TODO - Representation
    int count; // elements in the bag
    TElem* elements; //store elements
    int* frequencies; //store the frequencies
    int capacity;

	//DO NOT CHANGE THIS PART
	friend class BagIterator;
public:
	//constructor
	Bag(int capacity=10);

	//adds an element to the bag
	void add(TElem e);

	//removes one occurence of an element from a bag
	//returns true if an eleent was removed, false otherwise (if e was not part of the bag)
	bool remove(TElem e);

	//checks if an element appearch is the bag
	bool search(TElem e) const;

	//returns the number of occurrences for an element in the bag
	int nrOccurrences(TElem e) const;
    
    //adds nr occurrences of elem in the Bag.
    //throws an exception if nr is negative
    void addOccurrences(int nr, TElem elem);

	//returns the number of elements from the bag
	int size() const;

	//returns an iterator for this bag
	BagIterator iterator() const;

	//checks if the bag is empty
	bool isEmpty() const;
    
	//destructor
	~Bag();
    
private:
    // Resizes the vector, multiplying its capacity by a given factor (real number).
    void resize(double factor = 2);
    
};
