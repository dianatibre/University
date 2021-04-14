#include "Bag.h"
#include "BagIterator.h"
#include <exception>
#include <iostream>
using namespace std;


Bag::Bag(int capacity) {
	//TODO - Implementation
    this->count = 0;
    this->capacity=capacity;
    this->elements=new TElem[capacity];
    this->frequencies=new int[capacity];
    //for (int i=0; i<this->capacity; i++)
    //    this->frequencies[i]=0;
}


void Bag::add(TElem elem) {
	//TODO - Implementation
    if (this->count == this->capacity)
        this->resize();
    if (search(elem)==true) {
        for (int index=0;index<this->count;index++) {
            if (this->elements[index]==elem) {
                //int position=index;
                (this->frequencies[index])++;
                return;
            }
        }
        
    }
    else if (search(elem)==false) {
        this->elements[this->count]=elem;
        this->frequencies[this->count]=1;
        count++;
    }
}


bool Bag::remove(TElem elem) {
	//TODO - Implementation
    int position=0;
    if (search(elem)==false)
    {
        return false;
    }
    else
    {
        for (int index=0;index<this->count;index++){
            if (this->elements[index]==elem)
                position=index;
        }
        if (this->frequencies[position]==1) {
            for (int index=position; index<this->count-1; index++)
            {
                this->elements[index]=this->elements[index+1];
                this->frequencies[index]=this->frequencies[index+1];
            }
            (this->count)--;
        }
        else
            this->frequencies[position]--;
        return true;
    }
}


bool Bag::search(TElem elem) const {
	//TODO - Implementation
    int index;
    for (index=0;index<this->count;index++)
        if (this->elements[index]==elem)
            return true;
	return false; 
}

int Bag::nrOccurrences(TElem elem) const {
	//TODO - Implementation
    int position=0;
    int noOcc=0;
    if (search(elem)==false)
        return 0;
    for (int index=0;index<this->count;index++){
        if (this->elements[index]==elem)
            position=index;
            }
    //if (this->frequencies[position]!=0) {
    noOcc=this->frequencies[position];
    //}
    return noOcc;
}

void Bag::addOccurrences(int nr, TElem elem) {
    if ( nr < 0 ) {
        throw exception();
    }
    else
    {
        if (search(elem)==true)
        {
            for (int index=0;index<this->count;index++) {
                if (this->elements[index]==elem) {
                    this->frequencies[index]=this->frequencies[index]+nr;
                        return;
                }
        }
        }
        else if (search(elem)==false && nr!=0) {
            this->elements[this->count]=elem;
            this->frequencies[this->count]=nr;
            count++;
        }
    }
}


int Bag::size() const {
	//TODO - Implementation
    int nr=0;
    for (int i=0; i<this->count; i++)
        nr=nr+(this->frequencies[i]);
    return nr;
}

void Bag::resize(double factor)
{
    this->capacity *= static_cast<int>(factor);
    
    TElem* els = new TElem[this->capacity];
    int* fre = new int[this->capacity];
    for (int i = 0; i < this->count; i++) {
        els[i] = this->elements[i];
        fre[i] = this->frequencies[i];
    }
    delete[] this->elements;
    delete[] this->frequencies;
    this->elements = els;
    this->frequencies = fre;
}

bool Bag::isEmpty() const {
	//TODO - Implementation
    if (this->count==0)
        return true;
    else
        return false;
}

BagIterator Bag::iterator() const {
	//TODO - Implementation
	return BagIterator(*this);
}


Bag::~Bag() {
	//TODO - Implementation
    delete[] this->elements;
    delete[] this->frequencies;
}

