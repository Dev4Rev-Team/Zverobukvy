#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

def createImage():
    with open("words.txt", encoding='utf-8') as file:
        for item in file:
            word, name = item.split()
            print("word = " + word + "   name "+ name)
            center(word, "./img/"+name+".jpg")

        
def center(text, output_path):
    width, height = (512, 512)
    image = Image.new("RGB", (width, height), "white")
    draw = ImageDraw.Draw(image)
    font = ImageFont.truetype("Kardinal.ttf", size=155)
    (font_width, font_height), (offset_x, offset_y) = font.font.getsize(text)
    new_width = (width - font_width) / 2
    new_height = (height - font_height) / 2 - 0.21*font_height
    draw.text((new_width, new_height), text,font=font, fill="black")
    image.save(output_path)

if __name__ == "__main__":
    Path("./img").mkdir(parents=True, exist_ok=True)
    createImage()