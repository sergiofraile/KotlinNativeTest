//
//  ViewController.swift
//  KotliniOS
//
//  Created by Ellen Shapiro on 10/15/18.
//  Copyright © 2018 Bakken & Baeck. All rights reserved.
//

import UIKit
import SharedCode

class ViewController: UIViewController {
    
    @IBOutlet private var helloLabel: UILabel!
    @IBOutlet private var isOpenLabel: UILabel!
    
    @IBOutlet private var dayHeaderLabel: UILabel!
    @IBOutlet private var startHeaderLabel: UILabel!
    @IBOutlet private var enddHeaderLabel: UILabel!
    
    @IBOutlet private var dayStackView: UIStackView!
    @IBOutlet private var startTimeStackView: UIStackView!
    @IBOutlet private var endTimeStackView: UIStackView!
    
    @IBOutlet private var enableButton: UIButton!
    @IBOutlet private var disableButton: UIButton!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        HttpBinClient().runGet()
        HttpBinClient().runPost(body: "HELLO iOS!")
        
        self.helloLabel.text = CommonKt.createApplicationScreenMessage()
        
        let hours = HoursOfOperation.Weekdays(fromHour: 8.0, toHour: 20.0)
        configureStacks(for: hours)
        
        let companion = HoursOfOperation.Companion()
        dayHeaderLabel.text = companion.dayHeaderTitle()
        startHeaderLabel.text = companion.openingHeaderTitle()
        enddHeaderLabel.text = companion.closingHeaderTitle()
        
        self.isOpenLabel.text = hours.isOpenText(onDay: .saturday, atHour: 23.5)
    }
    
    func configureStacks(for hours: HoursOfOperation) {
        // Clear anything existing
        dayStackView.clear()
        startTimeStackView.clear()
        endTimeStackView.clear()
        
        printHoursType(hours)
        
        hours.hours.forEach { dayHours in
            self.printForDay(dayHours.forDay)
            
            let dayLabel = UILabel()
            dayLabel.text = dayHours.dayString
            dayStackView.addArrangedSubview(dayLabel)
            
            let openingLabel = UILabel()
            openingLabel.text = dayHours.startHourString
            startTimeStackView.addArrangedSubview(openingLabel)
            
            let closingLabel = UILabel()
            closingLabel.text = dayHours.endHourString
            endTimeStackView.addArrangedSubview(closingLabel)
        }
    }
    
    @IBAction private func tapDisable() {
        self.disableButton.isEnabled = false
        self.enableButton.isEnabled = true
    }
    
    @IBAction private func tappedEnable() {
        self.disableButton.isEnabled = true
        self.enableButton.isEnabled = false
    }
    
    func printForDay(_ day: DayOfWeek) {
        switch day {
        case .monday:
            print("You can fall apart")
        case .tuesday,
             .wednesday:
            print("Break my heart")
        case .thursday:
            print("Doesn't even start")
        case .friday:
            print("I'm in love")
        case .saturday:
            print("Wait")
        case .sunday:
            print("Always comes too late")
        default:
            fatalError("Not a day")
        }
    }
    
    func printHoursType(_ hours: HoursOfOperation) {
        switch hours {
        case is HoursOfOperation.Always:
            print("Always")
        case is HoursOfOperation.Variable:
            print("Variable")
        case is HoursOfOperation.Weekdays:
            print("Weekdays")
        case is HoursOfOperation.Weekends:
            print("Weekends")
        default:
            fatalError("Not a recognized subclass")
        }
    }
}
