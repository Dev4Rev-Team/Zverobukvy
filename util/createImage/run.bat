call .\env\Scripts\activate.bat
echo start
@echo on
rm .\log.txt

.\env\Scripts\python create_image.py 

pause



