// <auto-generated>
//  automatically generated by the FlatBuffers compiler, do not modify
// </auto-generated>

namespace MyGame.Example
{

using global::System;
using global::FlatBuffers;

public struct Ability : IFlatbufferObject
{
  private Struct __p;
  public ByteBuffer ByteBuffer { get { return __p.bb; } }
  public void __init(int _i, ByteBuffer _bb) { __p = new Struct(_i, _bb); }
  public Ability __assign(int _i, ByteBuffer _bb) { __init(_i, _bb); return this; }

  public uint Id { get { return __p.bb.GetUint(__p.bb_pos + 0); } }
  public void MutateId(uint id) { __p.bb.PutUint(__p.bb_pos + 0, id); }
  public uint Distance { get { return __p.bb.GetUint(__p.bb_pos + 4); } }
  public void MutateDistance(uint distance) { __p.bb.PutUint(__p.bb_pos + 4, distance); }

  public static Offset<MyGame.Example.Ability> CreateAbility(FlatBufferBuilder builder, uint Id, uint Distance) {
    builder.Prep(4, 8);
    builder.PutUint(Distance);
    builder.PutUint(Id);
    return new Offset<MyGame.Example.Ability>(builder.Offset);
  }
};


}
