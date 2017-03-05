//
//  Task.swift
//  RealmTasks
//
//  Created by Sravani Anne on 3/4/17.
//  Copyright © 2017 Sravani Anne. All rights reserved.
//

import Foundation
import RealmSwift

class Task: Object {
    dynamic var name = ""
    dynamic var createdAt = NSDate()
    dynamic var notes = ""
    dynamic var isCompleted = false
    
// Specify properties to ignore (Realm won't persist these)
    
//  override static func ignoredProperties() -> [String] {
//    return []
//  }
}

class TaskList: Object {
    
    dynamic var name = ""
    dynamic var createdAt = NSDate()
    let tasks = List<Task>()
    
    // Specify properties to ignore (Realm won't persist these)
    
    //  override static func ignoredProperties() -> [String] {
    //    return []
    //  }
}
