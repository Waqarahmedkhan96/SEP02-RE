# EasyRide Relational Schema

This relational schema is aligned with the GR diagram and the EER diagram.

## Relations

**Employee**(`employeeId`, name, password)

- Primary key: `employeeId`

**DrivingLicense**(`licenseNo`, isCategoryA, isCategoryB, isCategoryC, isCategoryD)

- Primary key: `licenseNo`

**Customer**(`customerId`, name, phoneNo, email, cpr, passportNo, licenseNo)

- Primary key: `customerId`
- Foreign key: `licenseNo` references `DrivingLicense(licenseNo)`
- `licenseNo` should be unique because one customer has one driving license.

**Vehicle**(`vehicleId`, model, color, engine, plateNo, priceHour, deposit, noOfTire, lateFee, requiredLicense, condition, noOfSeats, currentState)

- Primary key: `vehicleId`
- `plateNo` should be unique.

**Booking**(`bookingId`, startDate, endDate, actualReturnDate, bookingStatus, customerId, vehicleId, employeeId)

- Primary key: `bookingId`
- Foreign key: `customerId` references `Customer(customerId)`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`
- Foreign key: `employeeId` references `Employee(employeeId)`
- Business rule: the same vehicle cannot have overlapping active bookings.

**Car**(`vehicleId`)

- Primary key: `vehicleId`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`

**MotorCycle**(`vehicleId`, hasSidecar, type)

- Primary key: `vehicleId`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`

**Van**(`vehicleId`, cargoVolume, hasSlidingDoor)

- Primary key: `vehicleId`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`

**Truck**(`vehicleId`, loadCapacity, trailerAttached)

- Primary key: `vehicleId`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`

**SportsCar**(`vehicleId`, topSpeed, hasTurbo)

- Primary key: `vehicleId`
- Foreign key: `vehicleId` references `Vehicle(vehicleId)`

## Notes

- `EasyRide` is not included as a table because it represents the system/company container in the domain model.
- `currentState` stores the vehicle state, for example available or rented.
- `lateFee` is stored on `Vehicle` because different vehicle types may have different late fees.
