#include <exception>
#include "BagIterator.h"
#include "Bag.h"

using namespace std;


BagIterator::BagIterator(const Bag& c): bag(c)
{
	//TODO - Implementation
    this->current_position=0;
    this->current_frequency=1;
}

void BagIterator::first() {
	//TODO - Implementation
    this->current_position=0;
    this->current_frequency=1;
}


void BagIterator::next() {
	//TODO - Implementation
    if (this->current_position<bag.count){
        if (this->current_frequency==bag.frequencies[this->current_position])
        {
            this->current_position++;
            this->current_frequency=1;
        }
        else
            this->current_frequency++;
    }
    else throw exception();
}


bool BagIterator::valid() const {
	//TODO - Implementation
    if (this->current_position==bag.count)
        return false;
    else
        return true;
}



TElem BagIterator::getCurrent() const
{
	//TODO - Implementation
    if (this->current_position<bag.count)
        return bag.elements[this->current_position];
    else
        throw exception();
	return NULL_TELEM 
}
