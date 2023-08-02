#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from pathlib import Path
from turtle import width
from PIL import Image, ImageDraw, ImageFont

file_with_words = "words.txt"
encoding='utf-8'
mode = "RGBA"
format = ".webp"
output_path = "./image"
fontFamily = "Kardinal.ttf"
maxSizeText = 500
stepDownSizeText = 5
fillText = '#151515'
percentageOfOccupiedSpaceWidth = 0.95
percentageOfOccupiedSpaceHeight = 0.95
width = 512
height = 512




def createImages():
    with open(file_with_words, encoding=encoding) as file:
        for item in file:
            word, name = item.split()
            print("word = " + word + "   name " + name)
            createImage(word, output_path + "/" + name)

        
def createImage(text, output_path):
    image = Image.new(mode, (width, height))
    drawText(image, text)
    image.save(output_path + format)
        

def drawText(image , text):
    width, height = image.size
    draw = ImageDraw.Draw(image)
    font = createFont(width * percentageOfOccupiedSpaceWidth, height * percentageOfOccupiedSpaceHeight, text, maxSizeText)
    (font_width, font_height), (offset_x, offset_y) = font.font.getsize(text)
    new_width = (width - font_width) / 2
    new_height = (height - font_height) / 2
    draw.text((new_width - offset_x, new_height - offset_y), text, font = font, fill = fillText)


def createFont(widthMax,heightMax, text, biginSizeFont):
    while True:
        font = ImageFont.truetype(fontFamily, size = biginSizeFont)
        (text_width, text_height), (offset_x, offset_y) = font.font.getsize(text)
        if(text_width > widthMax or text_height > heightMax):
            biginSizeFont -= stepDownSizeText
        else:
            print("size text = ", biginSizeFont)
            return font


if __name__ == "__main__":
    Path(output_path).mkdir(parents=True, exist_ok=True)
    createImages()