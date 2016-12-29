/**
 
 */
definition(
    name: "Projector On",
    namespace: "smokingcraterr",
    author: "smokingcraterr",
		description: "Set theater up when projector comes on",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Meta/light_outlet@2x.png"
)

preferences {
	section {
		input(name: "meter", type: "capability.powerMeter", title: "When This Power Meter...", required: true, multiple: false, description: null)
        input(name: "threshold", type: "number", title: "Reports Above...", required: true, description: "in either watts or kw.")
	}
    section {
    	input(name: "switches", type: "capability.switch", title: "Turn On These Switches", required: true, multiple: true, description: null)
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(meter, "power", meterHandler)
}

def meterHandler(evt) {
    def meterValue = evt.value as double
    def thresholdValue = threshold as int
    if (meterValue > thresholdValue) {
    	//if (switches.currentSwitch == 'off')
        if (switches.currentValue('switch').contains('off'))
        {
	    	log.debug "${meter} reported energy consumption above ${threshold}. Turning of switches."
    		switches.on()
        }
        else
        	{ log.debug "${meter} reported consumption above, but switch is already on."
            }
    }
}