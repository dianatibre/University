#include "SortedBagIterator.h"
#include "SortedBag.h"
#include <exception>

using namespace std;

SortedBagIterator::SortedBagIterator(const SortedBag& b) : bag(b) {
	//TODO - Implementation
    //inorder binary tree iterator - Left, Root, Right
    BSTNode* node = this->bag.root;
    while (node != nullptr) {
        this->stack.push(node);
        node = node->left;
    }
    if (!stack.empty()) {
        this->currentNode = this->stack.top();
    }
    else
        this->currentNode = nullptr;
}

TComp SortedBagIterator::getCurrent() {
	//TODO - Implementation
	if (this->valid()==false)
        throw exception();
    else
        return this->currentNode->info;
}

bool SortedBagIterator::valid() {
	//TODO - Implementation
	if (currentNode == nullptr)
        return false;
    else
        return true;
}

void SortedBagIterator::next() {
	//TODO - Implementation
    if (!this->valid())
        throw exception();
    else {
        BSTNode* node = this->stack.top();
        this->stack.pop();
        if (node->right != nullptr) {
            node = node->right;
            while (node != nullptr) {
                this->stack.push(node);
                node = node->left;
            }
        }
        if (!this->stack.empty())
            this->currentNode = this->stack.top();
        else
            this->currentNode = nullptr;
    }
}

void SortedBagIterator::first() {
	//TODO - Implementation
    while (!this->stack.empty())
        this->stack.pop();
    BSTNode* node = this->bag.root;
    while (node != nullptr) {
        this->stack.push(node);
        node = node->left;
    }
    if (!stack.empty())
        this->currentNode = this->stack.top();
    else
        this->currentNode = nullptr;
}

