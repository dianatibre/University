#include "Bag.h"
#include "ShortTest.h"
#include "ExtendedTest.h"
#include <iostream>

using namespace std;

int main() {

	testAll();
	cout << "Short tests over" << endl;
	testAllExtended();
    
    newImplementationTest();
    cout << "Test for new requirement over" << endl;

	cout << "All test over" << endl;
}
