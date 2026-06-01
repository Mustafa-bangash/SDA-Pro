### Student B: Individual Reflection Report
**Role:** Integration & Response Engineer

**Overview**
For the SDA-Pro project, I was responsible for building the services that react to security threats. My goal was to create a system that could connect to outside databases, figure out how bad an attack was, and automatically trigger the right defense actions. To keep the code clean and easy to update later, I used several specific design patterns. 

**1. Response Orchestration Service**
* **Facade Pattern:** I used this to act like a front desk receptionist. Instead of making the rest of our system talk to five different complex parts, I built one simple assessAndRespond command.
* **Strategy Pattern:** I used this because we cannot treat every threat the same way. It acts like a set of playbooks, swapping between an AggressiveContainmentStrategy and a ConservativeStrategy based on the threat level. 
* **Decorator Pattern:** I used this to add extra features to our actions without breaking the original code. I created an AuditLogDecorator that wraps around a base action to automatically record a security log.

**2. Threat Intel Service**
* **Adapter Pattern:** Outside APIs all speak different languages. I used the Adapter pattern to take the weird data from VirusTotal and translate it into the standard ReputationResult format that our system understands.
* **Proxy Pattern:** External APIs will block you if you ask them for information too many times in a row. I built a CachingProxy to check its own memory first before asking the outside API, saving us time and keeping us under the API rate limits.

**3. Notification Service**
* **Abstract Factory Pattern:** I used this to create "combo meals" of notifications. The EnterpriseNotificationFactory groups Email, Slack, and PagerDuty together into one family so we can set up all channels at once.

**4. Middleware Pipeline**
* **Chain of Responsibility Pattern:** I used this to build an assembly line for incoming security alerts. Instead of writing one massive block of code, the alert moves down a line of individual workers (Deduplication, Threat Intel, Classification).
