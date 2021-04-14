#include "programmer.h"



Programmer::Programmer()
{
}


Programmer::~Programmer()
{
}

std::string Programmer::toString() const
{
	return this->name + "," + std::to_string(this->id) + "\n";
}
