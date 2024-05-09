package com.example.tailoringmanagement.upperbody

class WaistCoat(
    frontLen: Float,
    backLen: Float,
    chest: Float,
    waist: Float,
    seat: Float,
    shoulderWidth: Float,
    armHole: Float,
    var individualShoulder: Float,
    var firstButtonPos: Float,
    var frontWidth: Float,
    var backWidth: Float
) : SleeveLess(frontLen, backLen, chest, waist, seat, shoulderWidth, armHole) {
    // No additional code is required in the class body
}
