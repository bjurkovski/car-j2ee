Parameters to use with the default configuration files (if not already set):

# Database Name: banque_dev
# Database Address: localhost:3306/banque_dev
# Database User: root
# Database Password: root

Instructions:

# Needs to have the gwt-2.4.0 folder in the lib directory (DO NOT COMMIT!)
# For development add the following line to Main.gwt.xml (reduces time of compilation, "gecko-1.8" for mozilla and "safari" for chrome)
# <set-property name="user.agent" value="safari"></set-property>

# Needs to have the JavaMail lib in the lib directory (DO NOT COMMIT!)
# Download: http://download.oracle.com/otn-pub/java/javamail/1.4.4//javamail1_4_4.zip
# Unpack in the lib directory and import "smtp.jar" to Banque-ejb

# To use the EJBs, in the server-side of the frontend create a instance variable of a manager (one of the "Enterprise Beans") but instead
# of initializing it annotate it with @EJB. eg.
# @EJB
# ClientManager clientManager;

Attention:
# Although setId() and an empty constructor are publicly available in the DTOs, they should *not* be used, they are for internal purposes
# only. When a DTO is created using a manager, it's ID will be set automatically
