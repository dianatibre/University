#include "PracticOOP.h"
#include <QtWidgets/QApplication>
#include "service.h"

int main(int argc, char *argv[])
{
    Service* service = new Service{ new Repository{} };
    QApplication a(argc, argv);
    std::vector<PracticOOP*> forms;
    for (auto i : service->repo->programmers)
        forms.push_back(new PracticOOP{ i, service });
    for (int i = 0; i < forms.size(); i++)
        forms[i]->show();
    return a.exec();
}
