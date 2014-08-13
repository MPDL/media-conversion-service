magick-service
==============

### Intro
Web service to use functionnality of [Imagemagick](http://www.imagemagick.org/). So far, the service implements only the [convert](http://www.imagemagick.org/script/convert.php) command

### Installation

1. Install Imagemagick [download](http://www.imagemagick.org/script/binary-releases.php)
2. Install Maven [download](http://maven.apache.org/download.cgi)
3. Install Tomcat [download](http://maven.apache.org/download.cgi)
4. clone https://github.com/MPDL/magick-service
5. Compile the service: In service directory, run `mvn clean install`
6. Copy magick.war in Tomcat Webapp directory
7. Create File `magick.properties` in Tomcat conf directory
8. Edit `magick.properties` with Property `imagemagick.convert.bin = /path/to/convert` (for instance `/usr/bin/convert`)
9. Start Tomcat
10. Service runs under `http://localhost:8080/magick`

### Usage
The magick service implemtents the method `GET` and `POST`. The parameters are:
- **url** (Mandotory for `GET`): the url of the file to be transformed
- **size**: As defined by imagemagick [resize](http://www.imagemagick.org/script/command-line-options.php#resize)
- **crop**:As defined by imagemagick [crop](http://www.imagemagick.org/script/command-line-options.php#crop)
- **format**: The format in which the file shhould be returned (for instance png, jpg, etc.)
- **priority** (size|crop): The method (size of crop) which is processed first (only relevant when resize and crop are both used)


 
