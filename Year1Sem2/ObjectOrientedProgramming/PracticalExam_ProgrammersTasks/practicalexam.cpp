#include "practicalexam.h"
#include "ui_practicalexam.h"

practicalExam::practicalExam(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::practicalExam)
{
    ui->setupUi(this);
}

practicalExam::~practicalExam()
{
    delete ui;
}

