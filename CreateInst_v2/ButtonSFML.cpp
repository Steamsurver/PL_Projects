#include "InterfacesCalculator.h"
#include <iostream>
#include <SFML/Graphics.hpp>
#include <SFML/Network.hpp>
#include "windows.h"
#include <fstream>


using namespace std;
using namespace sf;
#include "FormSFML.h"

Button_SFML::Button_SFML() {

}

Button_SFML::Button_SFML(int x, int y, double width, double height, string text, Font font, int textSize){
	pos.x = x;
	pos.y = y;
	size.x = width;
	size.y = height;
	bufferText = text;

	//параметры текста кнопки---------------------------------------------------

	
	currentText.setFont(font);
	currentText.setCharacterSize(textSize);
	currentText.setOutlineColor(Color::Black);
	currentText.setFillColor(Color::Black);
	currentText.setPosition(pos.x + (size.x *0.3) - 3, pos.y + (size.y * 0.3) - 3);
	currentText.setString(bufferText);
	//--------------------------------------------------------------------------


	BOX.setSize(size);
	BOX.setPosition(pos.x, pos.y);
	BOX.setFillColor(Color(150, 150, 150));
	BOX.setOutlineThickness(2);
	BOX.setOutlineColor(Color(30, 30, 30));

}


void Button_SFML::setPos(int x, int y) {
	pos.x = x;
	pos.y = y;
}

void Button_SFML::setSize(double width, double height) {
	size.x = width;
	size.y = height;
}

string Button_SFML::getName() {
	return bufferText;
}


bool Button_SFML::select(Vector2i mouseCoords) {

	if ((mouseCoords.x > pos.x && mouseCoords.x < pos.x + size.x) && (mouseCoords.y > pos.y && mouseCoords.y < pos.y + size.y)){
		focused = true;
		return  true;
	}
	else {
		focused = false;
		return false;
	}



}

void Button_SFML::setColor(Vector3f color) {
	this->color = color;
	BOX.setFillColor(Color(this->color.x, this->color.y, this->color.z));
}


Text Button_SFML::displayTextButton() {
	return currentText;
}


RectangleShape Button_SFML::displayButton() {
	if (focused) {
		BOX.setFillColor(Color(100,100,100));
	}
	if (!focused) {
		BOX.setFillColor(Color(150, 150, 150));
	}

	return BOX;
}

RectangleShape Button_SFML::displayButton(bool focused) {
	this->focused = focused;
	if (this->focused) {
		BOX.setFillColor(Color(100, 100, 100));
	}
	if (!this->focused) {
		BOX.setFillColor(Color(150, 150, 150));
	}

	return BOX;
}