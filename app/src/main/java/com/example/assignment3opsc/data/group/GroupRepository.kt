package com.example.assignment3opsc.data.group

class GroupRepository(private val dao: GroupDao) {
    fun groups() = dao.getGroups()
    suspend fun create(name: String, emoji: String) = dao.insert(GroupEntity(name = name, emoji = emoji))
    suspend fun delete(id: Long) = dao.deleteById(id)
}
