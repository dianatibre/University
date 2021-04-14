#pragma once
#include "repository.h"
#include <string>

using namespace std;

class Service
{
public:
    Repository* repo;
    Service();
    Service(Repository* r) : repo(r) {}
    ~Service();
    std::vector<Task> sortS();
    void removeTask(std::string d, std::string i);
    void updateTask(std::string d, std::string s, std::string i);
    static int taskCompare(const Task & a, const Task & b);
};



